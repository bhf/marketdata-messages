package com.bht.md.samples;

import com.bht.md.aeron.AeronPublisher;
import com.bht.md.aeron.AeronSubscriber;
import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.handlers.SimpleEchoHandler;
import com.bht.md.perfmeta.NoOpPerfMetaManager;
import com.bht.md.rbhandler.IdentifiableAgronaSBEDecodingHandler;
import com.bht.md.rbpublishers.SBEToAgronaIdentifiableRingbufferFragmentHandler;
import com.bht.md.rbpublishers.SBEToAgronaRingbufferFragmentHandler;
import com.bht.md.ringbuffers.IdentifyingManyToOneRingBuffer;
import com.bht.md.util.IdleStrategyUtil;
import com.bht.md.util.ThreadFactoryUtil;
import io.aeron.logbuffer.FragmentHandler;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.agrona.BufferUtil;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.ManyToOneRingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An Aeron term buffer being read from a subscriber is dropped onto an Agrona ManyToOneRingBuffer
 * by doing a buffer copy through AbstractMutableDirectBuffer (which goes down into a UNSAFE.copyMemory)
 *
 * The ringbuffer is processed from another thread, the decoding of the SBE message is done on that thread
 */
public class PerfTestSubscriberAgrona {
    private final Logger logger = LoggerFactory.getLogger(PerfTestSubscriberAgrona.class);
    private final boolean USE_ECHO_BACK_HANDLER = true;

    public static void main(String[] args) {
        String prefix = null != args ? args[0] : "";
        String logName = null != args ? prefix + "-Agrona-Subscriber" : "Agrona-Subscriber";
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

        PerfTestSubscriberAgrona perfTestSubscriberAgrona = new PerfTestSubscriberAgrona();
        perfTestSubscriberAgrona.setupSubscriberForTesting(
                streamId, channel, embedded, subscriberCpuId, echoChanId, echoStreamId, prefix);
    }

    private void setupSubscriberForTesting(
            int streamId,
            String channel,
            boolean embedded,
            int subscriberCpuId,
            String echoChanId,
            int echoStreamId,
            String prefix) {
        IdleStrategy idleStrat = IdleStrategyUtil.getIdleStrategy();

        logger.info("Starting Agrona based subscriber");

        AeronSubscriber sub = new AeronSubscriber(streamId, channel, embedded, idleStrat);
        IdentifyingManyToOneRingBuffer rb = buildAgronaSessionIdRingBuffer();

        // ManyToOneRingBuffer rb = buildAgronaRingBuffer();
        // FragmentHandler handler=getSubscriberHandler(rb);

        FragmentHandler handler = getSubscriberHandlerSessionId(rb);
        String echoHandlerId = prefix + "-EchoHandler-Agrona-" + subscriberCpuId;
        AbstractSBEDecodingHandler sbeHandler = getSBEHandler(echoChanId, echoStreamId, echoHandlerId);
        // startAgronaReader(new AgronaSBEDecodingHandler(sbeHandler,rb), IdleStrategyUtil.getIdleStrategy());

        startAgronaReader(new IdentifiableAgronaSBEDecodingHandler(sbeHandler, rb), IdleStrategyUtil.getIdleStrategy());
        startSubscriber(sub, handler, subscriberCpuId, IdleStrategyUtil.getIdleStrategy());
    }

    private void startAgronaReader(
            IdentifiableAgronaSBEDecodingHandler agronaSBEDecodingHandler, IdleStrategy idleStrategy) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    idleStrategy.reset();
                    agronaSBEDecodingHandler.processRingbuffer();
                    idleStrategy.idle();
                }
            }
        });
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

    private ManyToOneRingBuffer buildAgronaRingBuffer() {
        ByteBuffer b = BufferUtil.allocateDirectAligned(768 + 4096, 8);
        return new ManyToOneRingBuffer(new UnsafeBuffer(b));
    }

    private IdentifyingManyToOneRingBuffer buildAgronaSessionIdRingBuffer() {
        ByteBuffer b = BufferUtil.allocateDirectAligned(768 + 4096, 8);
        return new IdentifyingManyToOneRingBuffer(new UnsafeBuffer(b));
    }

    private FragmentHandler getSubscriberHandler(ManyToOneRingBuffer rb) {
        return new SBEToAgronaRingbufferFragmentHandler(rb);
    }

    private FragmentHandler getSubscriberHandlerSessionId(IdentifyingManyToOneRingBuffer rb) {
        return new SBEToAgronaIdentifiableRingbufferFragmentHandler(rb);
    }

    private void startSubscriber(AeronSubscriber sub, FragmentHandler handler, int subscriberCpuId, IdleStrategy idle) {
        sub.init();
        logger.info("Initialised subscriber");

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
