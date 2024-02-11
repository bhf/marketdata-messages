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
 * Write the histogram into chronicle
 * TODO: Change variable names, provide formatted version like in the original
 */
public class HistogramOutputMarshallable implements WriteBytesMarshallable, ReadBytesMarshallable {

    private double val1;
    private double val2;
    private double val3;
    private double val4;
    private int tid;
    private long msgCount;
    private long time;

    @Override
    public void readMarshallable(BytesIn<?> bytes)
            throws IORuntimeException, BufferUnderflowException, IllegalStateException, InvalidMarshallableException {
        time = bytes.readLong();
        msgCount = bytes.readLong();
        tid = bytes.readInt();
        val1 = bytes.readDouble();
        val2 = bytes.readDouble();
        val3 = bytes.readDouble();
        val4 = bytes.readDouble();
    }

    @Override
    public void writeMarshallable(BytesOut<?> bytes)
            throws IllegalStateException, BufferOverflowException, InvalidMarshallableException {
        bytes.writeLong(time);
        bytes.writeLong(msgCount);
        bytes.writeInt(tid);
        bytes.writeDouble(val1);
        bytes.writeDouble(val2);
        bytes.writeDouble(val3);
        bytes.writeDouble(val4);
    }

    public void updateValues(double val1, double val2, double val3, double val4, int tid, long msgCount, long time) {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.val4 = val4;
        this.tid = tid;
        this.msgCount = msgCount;
        this.time = time;
    }
}
