package com.bht.md.perfmeta;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;

/** Write out performance metrics and details to a Chronicle queue */
public class ChronicleBasedPerfManager implements PerformanceMetaManager {
    private final MessageRoundtripMarshallable msgRtMarshall = new MessageRoundtripMarshallable();
    private final TestcaseDetailsMarshallable testDetailsMarshall = new TestcaseDetailsMarshallable();
    private final TimeOffsetsMarshallable timeOffsetsMarshall = new TimeOffsetsMarshallable();
    private final AeronPublisherCountersMarshallable aeronCountersMarshall = new AeronPublisherCountersMarshallable();
    private final PublisherDetailsMarshallable publisherDetailsMarshall = new PublisherDetailsMarshallable();
    private final ExcerptAppender rtAppender;
    private final ExcerptAppender testCaseDetailsAppender;
    private final ExcerptAppender timeOffsetsAppender;
    private final ExcerptAppender aeronCountersAppender;
    private final ExcerptAppender publisherDetailsAppender;

    public ChronicleBasedPerfManager(
            ChronicleQueue roundtripQ,
            ChronicleQueue testcaseDetailsQ,
            ChronicleQueue timeoffsetsQ,
            ChronicleQueue aeronCtrsQ,
            ChronicleQueue publisherDetsQ) {

        this.rtAppender = null != roundtripQ ? roundtripQ.createAppender() : null;
        this.testCaseDetailsAppender = null != testcaseDetailsQ ? testcaseDetailsQ.createAppender() : null;
        this.timeOffsetsAppender = null != timeoffsetsQ ? timeoffsetsQ.createAppender() : null;
        this.aeronCountersAppender = null != aeronCtrsQ ? aeronCtrsQ.createAppender() : null;
        this.publisherDetailsAppender = null != publisherDetsQ ? publisherDetsQ.createAppender() : null;
    }

    @Override
    public void handleMsgRT(int tid, int streamId, int sessionId, long ts) {
        msgRtMarshall.update(tid, streamId, sessionId, ts);
        rtAppender.writeBytes(msgRtMarshall);
    }

    @Override
    public void handleTestcaseDetails(String cfg) {
        testDetailsMarshall.setConfigFileUsed(cfg);
        testCaseDetailsAppender.writeBytes(testDetailsMarshall);
    }

    @Override
    public void handleTimeOffsetsMarshall(long start, long endWarmup) {
        timeOffsetsMarshall.update(start, endWarmup);
        timeOffsetsAppender.writeBytes(timeOffsetsMarshall);
    }

    @Override
    public void handleAeronCounters(
            long ts,
            long backPressure,
            long totalMsgs,
            long maxPosExceededCount,
            long pubClosedCount,
            long adminActionCount,
            long pubNotConnectedCount) {

        aeronCountersMarshall.update(
                ts,
                backPressure,
                totalMsgs,
                maxPosExceededCount,
                pubClosedCount,
                adminActionCount,
                pubNotConnectedCount);
        aeronCountersAppender.writeBytes(aeronCountersMarshall);
    }

    @Override
    public void handlePublisherDetails(int session, int stream, String chan, String publisherName) {
        publisherDetailsMarshall.update(chan, stream, session, publisherName);
        publisherDetailsAppender.writeBytes(publisherDetailsMarshall);
    }
}
