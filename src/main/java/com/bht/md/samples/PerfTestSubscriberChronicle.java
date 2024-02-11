package com.bht.md.samples;

import com.bht.md.aeron.AeronPublisher;
import com.bht.md.aeron.AeronSubscriber;
import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.handlers.SimpleEchoHandler;
import com.bht.md.perfmeta.NoOpPerfMetaManager;
import com.bht.md.rbhandler.ChronicleSBEDecodingHandler;
import com.bht.md.rbpublishers.SBEToChronicleQueueFragmentHandler;
import com.bht.md.util.IdleStrategyUtil;
import com.bht.md.util.ThreadFactoryUtil;
import io.aeron.logbuffer.FragmentHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An Aeron term buffer being read from a subscriber is dropped onto a Chronicle queue by doing a
 * buffer copy via Chronicle's WritesBytesMarshallable, which hits UNSAFE after several layers of
 * indirection
 *
 * <p>The queue is processed from another thread, the decoding of the SBE message is done on that
 * thread
 */
public class PerfTestSubscriberChronicle {
    final Logger logger = LoggerFactory.getLogger(PerfTestSubscriberChronicle.class);
    private static final boolean USE_ECHO_BACK_HANDLER = true;

    public static void main(String[] args) {

        String prefix = null != args ? args[0] : "";
        String logName = null != args ? prefix + "-Chronicle-Subscriber" : "Chronicle-Subscriber";
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

        String basePath = null != args ? args[7] : "/dev/shm/";

        PerfTestSubscriberChronicle perfTest = new PerfTestSubscriberChronicle();
        perfTest.setupSubscriberForTesting(
                streamId, channel, embedded, basePath, subscriberCpuId, echoChanId, echoStreamId, prefix);
    }

    private void setupSubscriberForTesting(
            int streamId,
            String channel,
            boolean embedded,
            String basePath,
            int subscriberCpuId,
            String echoChan,
            int echoStream,
            String prefix) {
        IdleStrategy idleStrat = IdleStrategyUtil.getIdleStrategy();
        AeronSubscriber sub = new AeronSubscriber(streamId, channel, embedded, idleStrat);

        ChronicleQueue queue = buildChronicleQueue(basePath);
        String echoHandlerId = prefix + "-EchoHandler-Chronicle-" + subscriberCpuId;
        AbstractSBEDecodingHandler sqHandler = getSBEHandler(echoChan, echoStream, echoHandlerId);
        startChronicleSubscriber(queue, IdleStrategyUtil.getIdleStrategy(), sqHandler);

        FragmentHandler handler = getSubscriberHandler(queue);
        startSubscriber(sub, handler, subscriberCpuId, IdleStrategyUtil.getIdleStrategy());
    }

    private AbstractSBEDecodingHandler getSBEHandler(String chan, int stream, String echoHandlerName) {
        if (USE_ECHO_BACK_HANDLER) {
            boolean embedded = false;
            AeronPublisher pub = new AeronPublisher(
                    stream, chan, embedded, IdleStrategyUtil.getIdleStrategy(), true, new NoOpPerfMetaManager());
            pub.init();
            return new SimpleEchoHandler(pub, echoHandlerName);
        }
        return null;
    }

    private void startChronicleSubscriber(
            ChronicleQueue queue, IdleStrategy idleStrategy, AbstractSBEDecodingHandler sqHandler) {

        ChronicleSBEDecodingHandler decodingHandler = new ChronicleSBEDecodingHandler(queue.createTailer(), sqHandler);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    idleStrategy.reset();
                    decodingHandler.process();
                    idleStrategy.idle();
                }
            }
        });
    }

    private ChronicleQueue buildChronicleQueue(String basePath) {
        logger.info("Chronicle on base path:" + basePath);
        return SingleChronicleQueueBuilder.single(basePath).build();
    }

    private FragmentHandler getSubscriberHandler(ChronicleQueue queue) {
        return new SBEToChronicleQueueFragmentHandler(queue);
    }

    private void startSubscriber(AeronSubscriber sub, FragmentHandler handler, int subscriberCpuId, IdleStrategy idle) {
        sub.init();
        logger.info("Initialised Chronicle subscriber");

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
