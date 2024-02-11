package com.bht.md.samples.clean;

import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.perfmeta.PerformanceMetaManager;
import com.bht.md.rbhandler.DisruptorSBEDecodingHandler;
import com.bht.md.rbpublishers.BufferEvent;
import com.bht.md.rbpublishers.SBEToDisruptorFragmentHandler;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import io.aeron.logbuffer.FragmentHandler;
import java.nio.ByteBuffer;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;

public class DisruptorSubRT extends AbstractSubscriberRTTesting {

    private static final String DISRUPTOR = "Disruptor";
    RingBuffer<BufferEvent> rb;

    AbstractSBEDecodingHandler sqHandler;

    @Override
    public FragmentHandler getFragmentHandler(
            String chan, int stream, boolean embedded, boolean exclusivePub, PerformanceMetaManager perfMgr) {
        rb = buildDisruptor(sqHandler);
        return new SBEToDisruptorFragmentHandler(rb);
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

    @Override
    public String getParadigmName() {
        return DISRUPTOR;
    }

    @Override
    public void setupParadigmSpecific(
            int streamId,
            String channel,
            int echoStreamId,
            String echoChanId,
            boolean embedded,
            boolean exclusive,
            PerformanceMetaManager perfMgr) {
        sqHandler = getSBEDecodingHandler(echoChanId, echoStreamId, embedded, exclusive, perfMgr);
    }
}
