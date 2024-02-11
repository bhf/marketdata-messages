package com.bht.md.samples;

import com.bht.md.aeron.AeronPublisher;
import com.bht.md.aeron.AeronSubscriber;
import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.handlers.SimpleEchoHandler;
import com.bht.md.perfmeta.NoOpPerfMetaManager;
import com.bht.md.rbhandler.DisruptorSBEDecodingHandler;
import com.bht.md.rbpublishers.BufferEvent;
import com.bht.md.rbpublishers.SBEToDisruptorFragmentHandler;
import com.bht.md.util.IdleStrategyUtil;
import com.bht.md.util.ThreadFactoryUtil;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import io.aeron.logbuffer.FragmentHandler;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.agrona.BufferUtil;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An Aeron term buffer being read from a subscriber is put onto a Disruptor ringbuffer where
 * the elements are backed by individual Agrona DirectBuffers, which delegate down into
 * UNSAFE.copyMemory (through AbstractMutableDirectBuffer).
 *
 * The ringbuffer is processed from another thread, the decoding of the SBE message is done on that thread
 *
 * The difference between this and the Agrona version of the test is that the Agrona version using a single
 * large DirectBuffer (for it's ManyToOne based ringbuffer) as opposed to many smaller individual DirectBuffer
 * implementations in this version
 */
public class PerfTestSubscriberDisruptor {
    private final Logger logger = LoggerFactory.getLogger(PerfTestSubscriberDisruptor.class);
    private static final boolean USE_ECHO_BACK_HANDLER = true;

    public static void main(String[] args) {

        String prefix = null != args ? args[0] : "";
        String logName = null != args ? prefix + "-Disruptor-Subscriber" : "Disruptor-Subscriber";
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

        PerfTestSubscriberDisruptor perfTest = new PerfTestSubscriberDisruptor();
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
        String echoHandlerId = prefix + "-EchoHandler-Disruptor-" + subscriberCpuId;
        AbstractSBEDecodingHandler sqHandler = getSBEHandler(echoChan, echoStream, echoHandlerId);
        RingBuffer<BufferEvent> rb = buildDisruptor(sqHandler);
        FragmentHandler handler = getSubscriberHandler(rb);
        startSubscriber(sub, handler, subscriberCpuId, IdleStrategyUtil.getIdleStrategy());
    }

    private AbstractSBEDecodingHandler getSBEHandler(String chan, int stream, String echoHandlerID) {
        if (USE_ECHO_BACK_HANDLER) {
            boolean embedded = false;
            AeronPublisher pub = new AeronPublisher(
                    stream, chan, embedded, IdleStrategyUtil.getIdleStrategy(), true, new NoOpPerfMetaManager());
            pub.init();
            return new SimpleEchoHandler(pub, echoHandlerID);
        }
        return null;
    }

    private RingBuffer<BufferEvent> buildDisruptor(AbstractSBEDecodingHandler sqHandler) {

        EventFactory<BufferEvent> evtFactory = new EventFactory<BufferEvent>() {
            @Override
            public BufferEvent newInstance() {
                ByteBuffer b = BufferUtil.allocateDirectAligned(512, 8);
                return new BufferEvent(new UnsafeBuffer(b));
            }
        };

        ProducerType pt = ProducerType.SINGLE;
        WaitStrategy ws = new BusySpinWaitStrategy();
        Disruptor<BufferEvent> disruptor =
                new Disruptor<BufferEvent>(evtFactory, 4096, DaemonThreadFactory.INSTANCE, pt, ws);

        disruptor.handleEventsWith(new DisruptorSBEDecodingHandler(sqHandler));
        return disruptor.start();
    }

    private FragmentHandler getSubscriberHandler(RingBuffer<BufferEvent> rb) {
        return new SBEToDisruptorFragmentHandler(rb);
    }

    private void startSubscriber(AeronSubscriber sub, FragmentHandler handler, int subscriberCpuId, IdleStrategy idle) {
        sub.init();
        logger.info("Initialised Disruptor subscriber");

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
