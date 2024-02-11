package com.bht.md.perfmeta;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.ReadBytesMarshallable;
import net.openhft.chronicle.bytes.WriteBytesMarshallable;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.core.io.InvalidMarshallableException;

public class MessageRoundtripMarshallable implements WriteBytesMarshallable, ReadBytesMarshallable {

    int tid;
    int streamId;
    int sessionId;
    long timestamp;

    public void update(int tid, int stream, int sess, long ts) {
        this.tid = tid;
        this.streamId = stream;
        this.sessionId = sess;
        this.timestamp = ts;
    }

    @Override
    public void readMarshallable(BytesIn<?> bytes)
            throws IORuntimeException, BufferUnderflowException, IllegalStateException, InvalidMarshallableException {
        tid = bytes.readInt();
        streamId = bytes.readInt();
        sessionId = bytes.readInt();
        timestamp = bytes.readLong();
    }

    @Override
    public void writeMarshallable(BytesOut<?> bytes)
            throws IllegalStateException, BufferOverflowException, InvalidMarshallableException {
        bytes.writeInt(tid);
        bytes.writeInt(streamId);
        bytes.writeInt(sessionId);
        bytes.writeLong(timestamp);
    }
}
