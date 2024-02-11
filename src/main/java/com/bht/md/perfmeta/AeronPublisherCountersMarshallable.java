package com.bht.md.perfmeta;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.ReadBytesMarshallable;
import net.openhft.chronicle.bytes.WriteBytesMarshallable;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.core.io.InvalidMarshallableException;

public class AeronPublisherCountersMarshallable implements WriteBytesMarshallable, ReadBytesMarshallable {

    private long ts = 0;
    private long backPressureCount = 0;
    private long totalMessageCount = 0;
    private long maxPosExceededCount = 0;
    private long publicationClosedCount = 0;
    private long adminActionCount = 0;
    private long publisherNotConnectedCount = 0;

    public void update(
            long ts,
            long backPressureCount,
            long totalMessageCount,
            long maxPosExceededCount,
            long publicationClosedCount,
            long adminActionCount,
            long publisherNotConnectedCount) {
        this.ts = ts;
        this.backPressureCount = backPressureCount;
        this.totalMessageCount = totalMessageCount;
        this.maxPosExceededCount = maxPosExceededCount;
        this.publicationClosedCount = publicationClosedCount;
        this.adminActionCount = adminActionCount;
        this.publisherNotConnectedCount = publisherNotConnectedCount;
    }

    @Override
    public void readMarshallable(BytesIn<?> bytes)
            throws IORuntimeException, BufferUnderflowException, IllegalStateException, InvalidMarshallableException {
        ts = bytes.readLong();
        backPressureCount = bytes.readLong();
        totalMessageCount = bytes.readLong();
        maxPosExceededCount = bytes.readLong();
        publicationClosedCount = bytes.readLong();
        adminActionCount = bytes.readLong();
        publisherNotConnectedCount = bytes.readLong();
    }

    @Override
    public void writeMarshallable(BytesOut<?> bytes)
            throws IllegalStateException, BufferOverflowException, InvalidMarshallableException {
        bytes.writeLong(ts);
        bytes.writeLong(backPressureCount);
        bytes.writeLong(totalMessageCount);
        bytes.writeLong(maxPosExceededCount);
        bytes.writeLong(publicationClosedCount);
        bytes.writeLong(adminActionCount);
        bytes.writeLong(publisherNotConnectedCount);
    }
}
