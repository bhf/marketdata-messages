package com.bht.md.samples.clean;

import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.perfmeta.PerformanceMetaManager;
import com.bht.md.rbhandler.IdentifiableAgronaSBEDecodingHandler;
import com.bht.md.rbpublishers.SBEToAgronaIdentifiableRingbufferFragmentHandler;
import com.bht.md.ringbuffers.IdentifyingManyToOneRingBuffer;
import com.bht.md.util.IdleStrategyUtil;
import io.aeron.logbuffer.FragmentHandler;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import org.agrona.BufferUtil;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;

public class AgronaSubRT extends AbstractSubscriberRTTesting {

    IdentifyingManyToOneRingBuffer rb;

    @Override
    public FragmentHandler getFragmentHandler(
            String chan, int stream, boolean embedded, boolean exclusivePub, PerformanceMetaManager perfMgr) {
        return new SBEToAgronaIdentifiableRingbufferFragmentHandler(rb);
    }

    private IdentifyingManyToOneRingBuffer buildAgronaSessionIdRingBuffer() {
        ByteBuffer b = BufferUtil.allocateDirectAligned(768 + 4096, 8);
        return new IdentifyingManyToOneRingBuffer(new UnsafeBuffer(b));
    }

    @Override
    public String getParadigmName() {
        return "AgronaRB";
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
        rb = buildAgronaSessionIdRingBuffer();
        AbstractSBEDecodingHandler sbeHandler =
                getSBEDecodingHandler(echoChanId, echoStreamId, embedded, exclusive, perfMgr);
        startAgronaReader(new IdentifiableAgronaSBEDecodingHandler(sbeHandler, rb), IdleStrategyUtil.getIdleStrategy());
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
}
