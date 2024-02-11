package com.bht.md.samples.clean;

import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.perfmeta.PerformanceMetaManager;
import com.bht.md.rbhandler.ChronicleSBEDecodingHandler;
import com.bht.md.rbpublishers.SBEToChronicleQueueFragmentHandler;
import com.bht.md.util.IdleStrategyUtil;
import io.aeron.logbuffer.FragmentHandler;
import java.util.concurrent.Executors;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.agrona.concurrent.IdleStrategy;

public class ChronicleSubRT extends AbstractSubscriberRTTesting {

    public static final String CHRONICLE = "Chronicle";
    public String CHRONICLE_LOCATION = "/tmp/";
    private ChronicleQueue queue;

    @Override
    public FragmentHandler getFragmentHandler(
            String chan, int stream, boolean embedded, boolean exclusivePub, PerformanceMetaManager perfMgr) {
        return new SBEToChronicleQueueFragmentHandler(queue);
    }

    private ChronicleQueue buildChronicleQueue(String basePath) {
        return SingleChronicleQueueBuilder.single(basePath).build();
    }

    @Override
    public String getParadigmName() {
        return CHRONICLE;
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
        queue = buildChronicleQueue(CHRONICLE_LOCATION);
        AbstractSBEDecodingHandler sqHandler =
                getSBEDecodingHandler(echoChanId, echoStreamId, embedded, exclusive, perfMgr);
        startChronicleSubscriber(queue, IdleStrategyUtil.getIdleStrategy(), sqHandler);
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
}
