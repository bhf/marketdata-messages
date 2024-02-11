package com.bht.md.samples.clean;

import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.perfmeta.PerformanceMetaManager;
import io.aeron.logbuffer.FragmentHandler;

public interface SubscriberRTTesting {

    public AbstractSBEDecodingHandler getSBEDecodingHandler(
            String chan, int stream, boolean embedded, boolean exclusivePub, PerformanceMetaManager perfMgr);

    public FragmentHandler getFragmentHandler(
            String chan, int stream, boolean embedded, boolean exclusivePub, PerformanceMetaManager perfMgr);

    public void startProcessingSub();

    public String getParadigmName();

    public PerformanceMetaManager getPerfTracker();

    public void setupParadigmSpecific(
            int streamId,
            String channel,
            int echoStreamId,
            String echoChanId,
            boolean embedded,
            boolean exclusive,
            PerformanceMetaManager perfMgr);
}
