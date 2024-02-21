package com.bht.md.perfmeta;

public class NoOpPerfMetaManager implements PerformanceMetaManager {
    @Override
    public void handleMsgRT(int tid, int streamId, int sessionId, long sent, long rec) {}

    @Override
    public void handleTestcaseDetails(String cfg) {}

    @Override
    public void handleTimeOffsetsMarshall(long start, long endWarmup) {}

    @Override
    public void handleAeronCounters(
            long ts,
            long backPressure,
            long totalMsgs,
            long maxPosExceededCount,
            long pubClosedCount,
            long adminActionCount,
            long pubNotConnectedCount) {}

    @Override
    public void handlePublisherDetails(int session, int stream, String chan, String publisherName) {}
}
