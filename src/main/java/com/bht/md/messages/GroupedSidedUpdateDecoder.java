/* Generated SBE (Simple Binary Encoding) message codec. */
package com.bht.md.messages;

import org.agrona.DirectBuffer;

/**
 * Mass update for the qtys at different prices on either side of the book for a single instrument using repeating groups
 */
@SuppressWarnings("all")
public final class GroupedSidedUpdateDecoder {
    public static final int BLOCK_LENGTH = 12;
    public static final int TEMPLATE_ID = 6;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final GroupedSidedUpdateDecoder parentMessage = this;
    private DirectBuffer buffer;
    private int initialOffset;
    private int offset;
    private int limit;
    int actingBlockLength;
    int actingVersion;

    public int sbeBlockLength() {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId() {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId() {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion() {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType() {
        return "GroupedSidedUpdate";
    }

    public DirectBuffer buffer() {
        return buffer;
    }

    public int initialOffset() {
        return initialOffset;
    }

    public int offset() {
        return offset;
    }

    public GroupedSidedUpdateDecoder wrap(
            final DirectBuffer buffer, final int offset, final int actingBlockLength, final int actingVersion) {
        if (buffer != this.buffer) {
            this.buffer = buffer;
        }
        this.initialOffset = offset;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public GroupedSidedUpdateDecoder wrapAndApplyHeader(
            final DirectBuffer buffer, final int offset, final MessageHeaderDecoder headerDecoder) {
        headerDecoder.wrap(buffer, offset);

        final int templateId = headerDecoder.templateId();
        if (TEMPLATE_ID != templateId) {
            throw new IllegalStateException("Invalid TEMPLATE_ID: " + templateId);
        }

        return wrap(
                buffer,
                offset + MessageHeaderDecoder.ENCODED_LENGTH,
                headerDecoder.blockLength(),
                headerDecoder.version());
    }

    public GroupedSidedUpdateDecoder sbeRewind() {
        return wrap(buffer, initialOffset, actingBlockLength, actingVersion);
    }

    public int sbeDecodedLength() {
        final int currentLimit = limit();
        sbeSkip();
        final int decodedLength = encodedLength();
        limit(currentLimit);

        return decodedLength;
    }

    public int encodedLength() {
        return limit - offset;
    }

    public int limit() {
        return limit;
    }

    public void limit(final int limit) {
        this.limit = limit;
    }

    public static int timeId() {
        return 1;
    }

    public static int timeSinceVersion() {
        return 0;
    }

    public static int timeEncodingOffset() {
        return 0;
    }

    public static int timeEncodingLength() {
        return 8;
    }

    public static String timeMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static long timeNullValue() {
        return 0xffffffffffffffffL;
    }

    public static long timeMinValue() {
        return 0x0L;
    }

    public static long timeMaxValue() {
        return 0xfffffffffffffffeL;
    }

    public long time() {
        return buffer.getLong(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public static int symbId() {
        return 2;
    }

    public static int symbSinceVersion() {
        return 0;
    }

    public static int symbEncodingOffset() {
        return 8;
    }

    public static int symbEncodingLength() {
        return 1;
    }

    public static String symbMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static short symbNullValue() {
        return (short) 255;
    }

    public static short symbMinValue() {
        return (short) 0;
    }

    public static short symbMaxValue() {
        return (short) 254;
    }

    public short symb() {
        return ((short) (buffer.getByte(offset + 8) & 0xFF));
    }

    public static int marketStateId() {
        return 3;
    }

    public static int marketStateSinceVersion() {
        return 0;
    }

    public static int marketStateEncodingOffset() {
        return 9;
    }

    public static int marketStateEncodingLength() {
        return 1;
    }

    public static String marketStateMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "optional";
        }

        return "";
    }

    public short marketStateRaw() {
        return ((short) (buffer.getByte(offset + 9) & 0xFF));
    }

    public MarketState marketState() {
        return MarketState.get(((short) (buffer.getByte(offset + 9) & 0xFF)));
    }

    public static int quoteConditionId() {
        return 4;
    }

    public static int quoteConditionSinceVersion() {
        return 0;
    }

    public static int quoteConditionEncodingOffset() {
        return 10;
    }

    public static int quoteConditionEncodingLength() {
        return 1;
    }

    public static String quoteConditionMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "optional";
        }

        return "";
    }

    public short quoteConditionRaw() {
        return ((short) (buffer.getByte(offset + 10) & 0xFF));
    }

    public QuoteCondition quoteCondition() {
        return QuoteCondition.get(((short) (buffer.getByte(offset + 10) & 0xFF)));
    }

    public static int quotesRemainingId() {
        return 5;
    }

    public static int quotesRemainingSinceVersion() {
        return 0;
    }

    public static int quotesRemainingEncodingOffset() {
        return 11;
    }

    public static int quotesRemainingEncodingLength() {
        return 1;
    }

    public static String quotesRemainingMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static short quotesRemainingNullValue() {
        return (short) 255;
    }

    public static short quotesRemainingMinValue() {
        return (short) 0;
    }

    public static short quotesRemainingMaxValue() {
        return (short) 254;
    }

    public short quotesRemaining() {
        return ((short) (buffer.getByte(offset + 11) & 0xFF));
    }

    private final QuoteGroupDecoder quoteGroup = new QuoteGroupDecoder(this);

    public static long quoteGroupDecoderId() {
        return 6;
    }

    public static int quoteGroupDecoderSinceVersion() {
        return 0;
    }

    public QuoteGroupDecoder quoteGroup() {
        quoteGroup.wrap(buffer);
        return quoteGroup;
    }

    public static final class QuoteGroupDecoder
            implements Iterable<QuoteGroupDecoder>, java.util.Iterator<QuoteGroupDecoder> {
        public static final int HEADER_SIZE = 4;
        private final GroupedSidedUpdateDecoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        QuoteGroupDecoder(final GroupedSidedUpdateDecoder parentMessage) {
            this.parentMessage = parentMessage;
        }

        public void wrap(final DirectBuffer buffer) {
            if (buffer != this.buffer) {
                this.buffer = buffer;
            }

            index = 0;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + HEADER_SIZE);
            blockLength = (buffer.getShort(limit + 0, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
            count = (buffer.getShort(limit + 2, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
        }

        public QuoteGroupDecoder next() {
            if (index >= count) {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + blockLength);
            ++index;

            return this;
        }

        public static int countMinValue() {
            return 0;
        }

        public static int countMaxValue() {
            return 65534;
        }

        public static int sbeHeaderSize() {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength() {
            return 17;
        }

        public int actingBlockLength() {
            return blockLength;
        }

        public int count() {
            return count;
        }

        public java.util.Iterator<QuoteGroupDecoder> iterator() {
            return this;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return index < count;
        }

        public static int sidedQuoteId() {
            return 7;
        }

        public static int sidedQuoteSinceVersion() {
            return 0;
        }

        public static int sidedQuoteEncodingOffset() {
            return 0;
        }

        public static int sidedQuoteEncodingLength() {
            return 17;
        }

        public static String sidedQuoteMetaAttribute(final MetaAttribute metaAttribute) {
            if (MetaAttribute.PRESENCE == metaAttribute) {
                return "required";
            }

            return "";
        }

        private final SidedQuoteDecoder sidedQuote = new SidedQuoteDecoder();

        public SidedQuoteDecoder sidedQuote() {
            sidedQuote.wrap(buffer, offset + 0);
            return sidedQuote;
        }

        public StringBuilder appendTo(final StringBuilder builder) {
            if (null == buffer) {
                return builder;
            }

            builder.append('(');
            builder.append("sidedQuote=");
            final SidedQuoteDecoder sidedQuote = this.sidedQuote();
            if (sidedQuote != null) {
                sidedQuote.appendTo(builder);
            } else {
                builder.append("null");
            }
            builder.append(')');

            return builder;
        }

        public QuoteGroupDecoder sbeSkip() {

            return this;
        }
    }

    public String toString() {
        if (null == buffer) {
            return "";
        }

        final GroupedSidedUpdateDecoder decoder = new GroupedSidedUpdateDecoder();
        decoder.wrap(buffer, initialOffset, actingBlockLength, actingVersion);

        return decoder.appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder) {
        if (null == buffer) {
            return builder;
        }

        final int originalLimit = limit();
        limit(initialOffset + actingBlockLength);
        builder.append("[GroupedSidedUpdate](sbeTemplateId=");
        builder.append(TEMPLATE_ID);
        builder.append("|sbeSchemaId=");
        builder.append(SCHEMA_ID);
        builder.append("|sbeSchemaVersion=");
        if (parentMessage.actingVersion != SCHEMA_VERSION) {
            builder.append(parentMessage.actingVersion);
            builder.append('/');
        }
        builder.append(SCHEMA_VERSION);
        builder.append("|sbeBlockLength=");
        if (actingBlockLength != BLOCK_LENGTH) {
            builder.append(actingBlockLength);
            builder.append('/');
        }
        builder.append(BLOCK_LENGTH);
        builder.append("):");
        builder.append("time=");
        builder.append(this.time());
        builder.append('|');
        builder.append("symb=");
        builder.append(this.symb());
        builder.append('|');
        builder.append("marketState=");
        builder.append(this.marketState());
        builder.append('|');
        builder.append("quoteCondition=");
        builder.append(this.quoteCondition());
        builder.append('|');
        builder.append("quotesRemaining=");
        builder.append(this.quotesRemaining());
        builder.append('|');
        builder.append("quoteGroup=[");
        final int quoteGroupOriginalOffset = quoteGroup.offset;
        final int quoteGroupOriginalIndex = quoteGroup.index;
        final QuoteGroupDecoder quoteGroup = this.quoteGroup();
        if (quoteGroup.count() > 0) {
            while (quoteGroup.hasNext()) {
                quoteGroup.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        quoteGroup.offset = quoteGroupOriginalOffset;
        quoteGroup.index = quoteGroupOriginalIndex;
        builder.append(']');

        limit(originalLimit);

        return builder;
    }

    public GroupedSidedUpdateDecoder sbeSkip() {
        sbeRewind();
        QuoteGroupDecoder quoteGroup = this.quoteGroup();
        if (quoteGroup.count() > 0) {
            while (quoteGroup.hasNext()) {
                quoteGroup.next();
                quoteGroup.sbeSkip();
            }
        }

        return this;
    }
}
