package com.bht.md.samples;

import com.bht.md.aeron.AeronPublisher;
import com.bht.md.aeron.AeronSubscriber;
import com.bht.md.handlers.*;
import com.bht.md.perfmeta.NoOpPerfMetaManager;
import com.bht.md.util.IdleStrategyUtil;
import com.bht.md.util.ThreadFactoryUtil;
import io.aeron.logbuffer.FragmentHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Don't buffer the message from the Aeron term reader onto a ringbuffer or queue but do the SBE
 * decode and echo back on the same thread
 */
public class PerfTestSubscriberAeron {
    final Logger logger = LoggerFactory.getLogger(PerfTestSubscriberAeron.class);
    private static final boolean USE_ECHO_BACK_HANDLER = true;

    public static void main(String[] args) {
        String prefix = null != args ? args[0] : "";
        String logName = null != args ? prefix + "-Aeron-Subscriber" : "Aeron-Subscriber";
        System.setProperty("log.name", logName);

        int subscriberCpuId = 0;

        if (null != args) {
            subscriberCpuId = Integer.parseInt(args[1]);
        }

        int streamId = null != args ? Integer.parseInt(args[2]) : 1002;
        String channel = null != args ? args[3] : "aeron:ipc";

        int echoStreamId = null != args ? Integer.parseInt(args[4]) : 1003;
        String echoChanId = null != args ? args[5] : "aeron:ipc";

        boolean embedded = null != args ? Boolean.parseBoolean(args[6]) : false;

        PerfTestSubscriberAeron perfTest = new PerfTestSubscriberAeron();
        perfTest.setupSubscriberForTesting(
                streamId, channel, embedded, subscriberCpuId, echoChanId, echoStreamId, prefix);
    }

    private void setupSubscriberForTesting(
            int streamId,
            String channel,
            boolean embedded,
            int subscriberCpuId,
            String echoChan,
            int echoStream,
            String prefix) {
        IdleStrategy idleStrat = IdleStrategyUtil.getIdleStrategy();

        AeronSubscriber sub = new AeronSubscriber(streamId, channel, embedded, idleStrat);
        String echoHandlerId = prefix + "-EchoHandler-Aeron-" + subscriberCpuId;
        FragmentHandler handler = getSubscriberHandler(echoChan, echoStream, echoHandlerId);

        startSubscriber(sub, handler, IdleStrategyUtil.getIdleStrategy(), subscriberCpuId);
    }

    private FragmentHandler getSubscriberHandler(String chan, int stream, String echoHandlerId) {
        SBEDecodingHandler sbeDec = getSBEHandler(chan, stream, echoHandlerId);
        return new AeronSBEDecodingFragmentHandler(sbeDec);
    }

    private AbstractSBEDecodingHandler getSBEHandler(String chan, int stream, String echoHandlerId) {
        if (USE_ECHO_BACK_HANDLER) {
            boolean embedded = false;
            AeronPublisher pub = new AeronPublisher(
                    stream, chan, embedded, IdleStrategyUtil.getIdleStrategy(), true, new NoOpPerfMetaManager());
            pub.init();
            return new SimpleEchoHandler(pub, echoHandlerId);
        }
        return null;
    }

    private void startSubscriber(AeronSubscriber sub, FragmentHandler handler, IdleStrategy idle, int subscriberCpuId) {
        sub.init();
        logger.info("Initialised Aeron subscriber");

        ExecutorService es = subscriberCpuId > 0
                ? Executors.newSingleThreadExecutor(
                        ThreadFactoryUtil.getThreadFactory("Aeron-SBE Subscriber", subscriberCpuId))
                : Executors.newSingleThreadExecutor();

        es.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    idle.reset();
                    sub.doWork(handler);
                    idle.idle();
                }
            }
        });
    }
}
