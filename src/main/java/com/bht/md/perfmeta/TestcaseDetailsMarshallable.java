package com.bht.md.perfmeta;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.ReadBytesMarshallable;
import net.openhft.chronicle.bytes.WriteBytesMarshallable;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.core.io.InvalidMarshallableException;

public class TestcaseDetailsMarshallable implements WriteBytesMarshallable, ReadBytesMarshallable {

    String configFileUsed;

    public void setConfigFileUsed(String cfg) {
        this.configFileUsed = cfg;
    }

    @Override
    public void readMarshallable(BytesIn<?> bytes)
            throws IORuntimeException, BufferUnderflowException, IllegalStateException, InvalidMarshallableException {
        configFileUsed = bytes.readUtf8();
    }

    @Override
    public void writeMarshallable(BytesOut<?> bytes)
            throws IllegalStateException, BufferOverflowException, InvalidMarshallableException {
        bytes.writeUtf8(configFileUsed);
    }
}
