package com.bht.md.perfmeta;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.ReadBytesMarshallable;
import net.openhft.chronicle.bytes.WriteBytesMarshallable;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.core.io.InvalidMarshallableException;

public class PublisherDetailsMarshallable implements WriteBytesMarshallable, ReadBytesMarshallable {
    private String chan;
    private int stream;
    private int session;
    private String publisherName;

    @Override
    public void readMarshallable(BytesIn<?> bytes)
            throws IORuntimeException, BufferUnderflowException, IllegalStateException, InvalidMarshallableException {
        chan = bytes.readUtf8();
        stream = bytes.readInt();
        session = bytes.readInt();
        publisherName = bytes.readUtf8();
    }

    @Override
    public void writeMarshallable(BytesOut<?> bytes)
            throws IllegalStateException, BufferOverflowException, InvalidMarshallableException {
        bytes.writeUtf8(chan);
        bytes.writeInt(stream);
        bytes.writeInt(session);
        bytes.writeUtf8(publisherName);
    }

    public void update(String chan, int stream, int session, String publisherName) {
        this.chan = chan;
        this.stream = stream;
        this.session = session;
        this.publisherName = publisherName;
    }
}
