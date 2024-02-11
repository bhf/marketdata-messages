package com.bht.md.perfmeta;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.ReadBytesMarshallable;
import net.openhft.chronicle.bytes.WriteBytesMarshallable;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.core.io.InvalidMarshallableException;

/**
 * Compound state for test case or benchmark details
 */
public class CompoundMarshallable implements WriteBytesMarshallable, ReadBytesMarshallable {

    private final AeronPublisherCountersMarshallable aeronCounts = new AeronPublisherCountersMarshallable();
    private final HistogramOutputMarshallable histoOutput = new HistogramOutputMarshallable();
    private final MessageRoundtripMarshallable rtDets = new MessageRoundtripMarshallable();
    private final PublisherDetailsMarshallable pubDets = new PublisherDetailsMarshallable();
    private final TestcaseDetailsMarshallable testcaseDetails = new TestcaseDetailsMarshallable();
    private final TimeOffsetsMarshallable timeOffsetDets = new TimeOffsetsMarshallable();

    enum PerfTrackerMsgType {
        AERON_COUNTS,
        HISTOGRAM,
        ROUND_TRIP,
        PUBLISHER_DETAILS,
        TESTCASE_DETAILS,
        NONE,
        TIMEOFFSETS
    }

    @Override
    public void readMarshallable(BytesIn<?> bytes)
            throws IORuntimeException, BufferUnderflowException, IllegalStateException, InvalidMarshallableException {
        PerfTrackerMsgType t = bytes.readEnum(PerfTrackerMsgType.class);

        switch (t) {
            case HISTOGRAM: {
                histoOutput.readMarshallable(bytes);
                break;
            }
            case AERON_COUNTS: {
                aeronCounts.readMarshallable(bytes);
                break;
            }
            case PUBLISHER_DETAILS: {
                pubDets.readMarshallable(bytes);
                break;
            }
            case ROUND_TRIP: {
                rtDets.readMarshallable(bytes);
                break;
            }
            case TESTCASE_DETAILS: {
                testcaseDetails.readMarshallable(bytes);
                break;
            }
            case TIMEOFFSETS: {
                timeOffsetDets.readMarshallable(bytes);
                break;
            }
            default: {
            }
        }
    }

    private PerfTrackerMsgType lastMsgType = PerfTrackerMsgType.NONE;

    @Override
    public void writeMarshallable(BytesOut<?> bytes)
            throws IllegalStateException, BufferOverflowException, InvalidMarshallableException {
        bytes.writeEnum(lastMsgType);

        switch (lastMsgType) {
            case HISTOGRAM: {
                histoOutput.writeMarshallable(bytes);
                break;
            }
            case AERON_COUNTS: {
                aeronCounts.writeMarshallable(bytes);
                break;
            }
            case PUBLISHER_DETAILS: {
                pubDets.writeMarshallable(bytes);
                break;
            }
            case ROUND_TRIP: {
                rtDets.writeMarshallable(bytes);
                break;
            }
            case TESTCASE_DETAILS: {
                testcaseDetails.writeMarshallable(bytes);
                break;
            }
            case TIMEOFFSETS: {
                timeOffsetDets.writeMarshallable(bytes);
                break;
            }
            default: {
            }
        }
    }
}
