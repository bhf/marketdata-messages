/* Generated SBE (Simple Binary Encoding) message codec. */
package com.bht.md.messages;

import org.agrona.DirectBuffer;

/**
 * A two sided quote, bid and ask
 */
@SuppressWarnings("all")
public final class TwoSidedQuoteDecoder {
    public static final int BLOCK_LENGTH = 43;
    public static final int TEMPLATE_ID = 2;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final TwoSidedQuoteDecoder parentMessage = this;
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
        return "TwoSidedQuote";
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

    public TwoSidedQuoteDecoder wrap(
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

    public TwoSidedQuoteDecoder wrapAndApplyHeader(
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

    public TwoSidedQuoteDecoder sbeRewind() {
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

    public static int bidQuoteId() {
        return 5;
    }

    public static int bidQuoteSinceVersion() {
        return 0;
    }

    public static int bidQuoteEncodingOffset() {
        return 11;
    }

    public static int bidQuoteEncodingLength() {
        return 16;
    }

    public static String bidQuoteMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    private final BidQuoteDecoder bidQuote = new BidQuoteDecoder();

    public BidQuoteDecoder bidQuote() {
        bidQuote.wrap(buffer, offset + 11);
        return bidQuote;
    }

    public static int askQuoteId() {
        return 6;
    }

    public static int askQuoteSinceVersion() {
        return 0;
    }

    public static int askQuoteEncodingOffset() {
        return 27;
    }

    public static int askQuoteEncodingLength() {
        return 16;
    }

    public static String askQuoteMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    private final AskQuoteDecoder askQuote = new AskQuoteDecoder();

    public AskQuoteDecoder askQuote() {
        askQuote.wrap(buffer, offset + 27);
        return askQuote;
    }

    public String toString() {
        if (null == buffer) {
            return "";
        }

        final TwoSidedQuoteDecoder decoder = new TwoSidedQuoteDecoder();
        decoder.wrap(buffer, initialOffset, actingBlockLength, actingVersion);

        return decoder.appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder) {
        if (null == buffer) {
            return builder;
        }

        final int originalLimit = limit();
        limit(initialOffset + actingBlockLength);
        builder.append("[TwoSidedQuote](sbeTemplateId=");
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
        builder.append("bidQuote=");
        final BidQuoteDecoder bidQuote = this.bidQuote();
        if (bidQuote != null) {
            bidQuote.appendTo(builder);
        } else {
            builder.append("null");
        }
        builder.append('|');
        builder.append("askQuote=");
        final AskQuoteDecoder askQuote = this.askQuote();
        if (askQuote != null) {
            askQuote.appendTo(builder);
        } else {
            builder.append("null");
        }

        limit(originalLimit);

        return builder;
    }

    public TwoSidedQuoteDecoder sbeSkip() {
        sbeRewind();

        return this;
    }
}
