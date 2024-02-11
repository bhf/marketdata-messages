/* Generated SBE (Simple Binary Encoding) message codec. */
package com.bht.md.messages;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class SidedQuoteEncoder {
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final int ENCODED_LENGTH = 17;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private MutableDirectBuffer buffer;

    public SidedQuoteEncoder wrap(final MutableDirectBuffer buffer, final int offset) {
        if (buffer != this.buffer) {
            this.buffer = buffer;
        }
        this.offset = offset;

        return this;
    }

    public MutableDirectBuffer buffer() {
        return buffer;
    }

    public int offset() {
        return offset;
    }

    public int encodedLength() {
        return ENCODED_LENGTH;
    }

    public int sbeSchemaId() {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion() {
        return SCHEMA_VERSION;
    }

    public static int priceEncodingOffset() {
        return 0;
    }

    public static int priceEncodingLength() {
        return 8;
    }

    public static long priceNullValue() {
        return -9223372036854775808L;
    }

    public static long priceMinValue() {
        return -9223372036854775807L;
    }

    public static long priceMaxValue() {
        return 9223372036854775807L;
    }

    public SidedQuoteEncoder price(final long value) {
        buffer.putLong(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int qtyEncodingOffset() {
        return 8;
    }

    public static int qtyEncodingLength() {
        return 8;
    }

    public static long qtyNullValue() {
        return -9223372036854775808L;
    }

    public static long qtyMinValue() {
        return -9223372036854775807L;
    }

    public static long qtyMaxValue() {
        return 9223372036854775807L;
    }

    public SidedQuoteEncoder qty(final long value) {
        buffer.putLong(offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int sideEncodingOffset() {
        return 16;
    }

    public static int sideEncodingLength() {
        return 1;
    }

    public SidedQuoteEncoder side(final QuoteSide value) {
        buffer.putByte(offset + 16, (byte) value.value());
        return this;
    }

    public String toString() {
        if (null == buffer) {
            return "";
        }

        return appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder) {
        if (null == buffer) {
            return builder;
        }

        final SidedQuoteDecoder decoder = new SidedQuoteDecoder();
        decoder.wrap(buffer, offset);

        return decoder.appendTo(builder);
    }
}
