package com.bht.md.samples.clean;

import com.bht.md.aeron.AeronPublisher;
import com.bht.md.aeron.AeronSubscriber;
import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.handlers.SimpleEchoHandler;
import com.bht.md.perfmeta.PerformanceMetaManager;
import com.bht.md.util.IdleStrategyUtil;
import com.bht.md.util.ThreadFactoryUtil;
import io.aeron.logbuffer.FragmentHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.agrona.concurrent.IdleStrategy;

public abstract class AbstractSubscriberRTTesting implements SubscriberRTTesting {

    private int subscriberCpuId;
    AeronSubscriber sub;
    String echoHandlerId;
    FragmentHandler fragmentHandler;
    IdleStrategy idleStrat;

    public AbstractSBEDecodingHandler getSBEDecodingHandler(
            String chan, int stream, boolean embedded, boolean exclusivePub, PerformanceMetaManager perfMgr) {
        AeronPublisher pub =
                new AeronPublisher(stream, chan, embedded, IdleStrategyUtil.getIdleStrategy(), exclusivePub, perfMgr);
        pub.init();
        pub.persistPublisherSessionDetails(echoHandlerId);
        return new SimpleEchoHandler(pub, echoHandlerId);
    }

    public void setupSubToAeron(
            int streamId,
            String channel,
            int echoStreamId,
            String echoChannel,
            boolean embedded,
            boolean exclusive,
            PerformanceMetaManager perfMgr,
            int subscriberCpuId_) {
        idleStrat = IdleStrategyUtil.getIdleStrategy();
        sub = new AeronSubscriber(streamId, channel, embedded, idleStrat);

        fragmentHandler = getFragmentHandler(echoChannel, echoStreamId, embedded, exclusive, perfMgr);
        this.subscriberCpuId = subscriberCpuId_;
    }

    @Override
    public PerformanceMetaManager getPerfTracker() {
        return null;
    }

    @Override
    public void startProcessingSub() {

        final String paradigm = getParadigmName();
        sub.init();
        ExecutorService es = subscriberCpuId > 0
                ? Executors.newSingleThreadExecutor(
                        ThreadFactoryUtil.getThreadFactory(paradigm + "-SBE Subscriber", subscriberCpuId))
                : Executors.newSingleThreadExecutor();

        es.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    idleStrat.reset();
                    sub.doWork(fragmentHandler);
                    idleStrat.idle();
                }
            }
        });
    }
}
