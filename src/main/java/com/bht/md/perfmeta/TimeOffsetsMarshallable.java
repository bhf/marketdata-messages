package com.bht.md.perfmeta;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.ReadBytesMarshallable;
import net.openhft.chronicle.bytes.WriteBytesMarshallable;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.core.io.InvalidMarshallableException;

public class TimeOffsetsMarshallable implements WriteBytesMarshallable, ReadBytesMarshallable {

    long startupTime;
    long endOfWarmupTime;

    public void update(long st, long ew) {
        this.startupTime = st;
        this.endOfWarmupTime = ew;
    }

    @Override
    public void readMarshallable(BytesIn<?> bytes)
            throws IORuntimeException, BufferUnderflowException, IllegalStateException, InvalidMarshallableException {
        startupTime = bytes.readLong();
        endOfWarmupTime = bytes.readLong();
    }

    @Override
    public void writeMarshallable(BytesOut<?> bytes)
            throws IllegalStateException, BufferOverflowException, InvalidMarshallableException {
        bytes.writeLong(startupTime);
        bytes.writeLong(endOfWarmupTime);
    }
}
