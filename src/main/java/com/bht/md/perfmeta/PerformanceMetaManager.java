package com.bht.md.perfmeta;

public interface PerformanceMetaManager {
    void handleMsgRT(int tid, int streamId, int sessionId, long ts);

    void handleTestcaseDetails(String cfg);

    void handleTimeOffsetsMarshall(long start, long endWarmup);

    void handleAeronCounters(
            long ts,
            long backPressure,
            long totalMsgs,
            long maxPosExceededCount,
            long pubClosedCount,
            long adminActionCount,
            long pubNotConnectedCount);

    void handlePublisherDetails(int session, int stream, String chan, String publisherName);
}
