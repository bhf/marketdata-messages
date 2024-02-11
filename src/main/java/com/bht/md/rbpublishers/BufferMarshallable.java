package com.bht.md.rbpublishers;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.ReadBytesMarshallable;
import net.openhft.chronicle.bytes.WriteBytesMarshallable;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.core.io.InvalidMarshallableException;
import org.agrona.DirectBuffer;

/**
 * Wrap a DirectBuffer into a format to be written onto a Chronicle queue - a copy is made of the
 * term buffer via UNSAFE through AbstractMutableDirectBuffer.putBytes before being written to the
 * Chronicle queue. This is because writes to the Chronicle queue are asynch, so we can't simply
 * wrap the Aeron term buffer.
 */
public class BufferMarshallable implements WriteBytesMarshallable, ReadBytesMarshallable {
    private int offset;
    private int length;
    private byte[] dataBuffer = new byte[512];
    public int session;
    public int stream;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getDataBuffer() {
        return dataBuffer;
    }

    public void setDataBuffer(byte[] dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    @Override
    public void writeMarshallable(BytesOut<?> bytes)
            throws IllegalStateException, BufferOverflowException, InvalidMarshallableException {
        bytes.writeInt(offset);
        bytes.writeInt(length);
        bytes.write(dataBuffer);
        bytes.writeInt(session);
        bytes.writeInt(stream);
    }

    public void setRefs(int offset, int length, DirectBuffer data, int session, int stream) {
        this.offset = offset;
        this.length = length;
        data.getBytes(offset, dataBuffer, 0, length);
        this.session = session;
        this.stream = stream;
    }

    @Override
    public void readMarshallable(BytesIn<?> bytes)
            throws IORuntimeException, BufferUnderflowException, IllegalStateException, InvalidMarshallableException {
        offset = bytes.readInt();
        length = bytes.readInt();
        bytes.read(dataBuffer);
        session = bytes.readInt();
        stream = bytes.readInt();
    }
}
