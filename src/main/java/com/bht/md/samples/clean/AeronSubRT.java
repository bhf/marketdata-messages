package com.bht.md.samples.clean;

import com.bht.md.handlers.AeronSBEDecodingFragmentHandler;
import com.bht.md.handlers.SBEDecodingHandler;
import com.bht.md.perfmeta.PerformanceMetaManager;
import io.aeron.logbuffer.FragmentHandler;

public class AeronSubRT extends AbstractSubscriberRTTesting {
    private static final String AERON = "Aeron";

    @Override
    public FragmentHandler getFragmentHandler(
            String chan, int stream, boolean embedded, boolean exclusivePub, PerformanceMetaManager perfMgr) {
        SBEDecodingHandler sbeDec = getSBEDecodingHandler(chan, stream, embedded, exclusivePub, perfMgr);
        return new AeronSBEDecodingFragmentHandler(sbeDec);
    }

    @Override
    public String getParadigmName() {
        return AERON;
    }

    @Override
    public void setupParadigmSpecific(
            int streamId,
            String channel,
            int echoStreamId,
            String echoChanId,
            boolean embedded,
            boolean exclusive,
            PerformanceMetaManager perfMgr) {}
}
