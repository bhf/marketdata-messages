package com.bhf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bht.md.messages.*;
import com.bht.md.messages.encodehelpers.SingleSidedQuoteEncodeHelper;
import java.nio.ByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

/**
 * Compare the binary generated by your encoding chain to
 * a known binary. This is very chicken and egg - how do you get a
 * correctly encoded binary without an encoder.
 *
 * This style of testing may be useful once you have a collection
 * of known messages. For example, you have a mature product and
 * have recorded some correctly formed business domain messages which work,
 * when you are doing a new release you can check the encoding
 * is still the same for all messages, or those which have not been
 * changed as part of a release, against your collection of known
 * and validated binary messages
 *
 * This is also useful for developers to run as part of their local
 * process e.g. replay and check against a bunch of known messages
 * which should not have changed.
 */
public class RawMessageTestStyles {

    public static final int CAPACITY = 512;
    public static final int ALIGNMENT = 8;
    private static final int HEADER_OFFSET = 8;
    SingleSidedQuoteEncoder singleSidedQuoteEncoder = new SingleSidedQuoteEncoder();
    SingleSidedQuoteEncodeHelper singleSidedQuoteEncodeHelper = new SingleSidedQuoteEncodeHelper();
    MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    ByteBuffer directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(CAPACITY, ALIGNMENT);
    MutableDirectBuffer buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);

    /** Test encoding against constant known values that are hard coded */
    @Test
    @Tags({@Tag("EncoderRawMessageComparison"), @Tag("SingleSidedQuote")})
    public void testSingleSidedQuoteRawBinary() {

        // Use your favourite value injection methodology, if you have a
        // lot of known SBE messages then you may prefer to use a Stream of Arguments

        final long timeValue = 0;
        final short symbolId = 1;
        final QuoteCondition quoteCondition = QuoteCondition.Direct;
        final MarketState marketState = MarketState.CONTINUOUS_TRADING_MODE;
        final long price = 123;
        final long qty = 55;
        final QuoteSide quoteSide = QuoteSide.Ask;

        singleSidedQuoteEncodeHelper.encodeSingleSidedQuote(
                singleSidedQuoteEncoder,
                buffer,
                headerEnc,
                timeValue,
                symbolId,
                marketState,
                quoteCondition,
                price,
                qty,
                quoteSide);

        MutableDirectBuffer knownBinary = loadKnownSingleSidedQuoteBinary();
        if (null != knownBinary) {
            compareBinary(buffer, knownBinary);
        } else {
            // handle not loading the known binary elegantly, alternatively
            // throw something better than an assertion error from
            // the call to loadKnownSingleSidedQuoteBinary()
            // throw new AssertionError();
        }
    }

    private void compareBinary(MutableDirectBuffer buffer, MutableDirectBuffer knownBinary) {

        byte[] generated = buffer.byteArray();
        byte[] recorded = knownBinary.byteArray();

        // Note: if you can guarantee the recorded size buffer should be the same size
        // as the generated one, then go ahead and check size equality first
        // You would also need to check if your recorded version has a header or not as
        // we are passing in a buffer which has had a header applied

        int minIndex = Math.min(generated.length, recorded.length);
        int i = 0;

        boolean skipHeader = true;

        if (skipHeader) {
            i = HEADER_OFFSET;
        } else {
            // decode the header and compare header fields if needed
            compareHeader(buffer, knownBinary);
        }

        // byte wise comparison post header
        for (; i < minIndex; i++) {
            assertEquals(generated[i], recorded[i]);
        }
    }

    private static void compareHeader(MutableDirectBuffer buffer, MutableDirectBuffer knownBinary) {
        MessageHeaderDecoder headerToTest = new MessageHeaderDecoder();
        headerToTest.wrap(buffer, 0);

        MessageHeaderDecoder knownBinaryHeader = new MessageHeaderDecoder();
        knownBinaryHeader.wrap(knownBinary, 0);

        assertEquals(knownBinaryHeader.templateId(), headerToTest.templateId());
        assertEquals(knownBinaryHeader.version(), headerToTest.version());
        assertEquals(knownBinaryHeader.schemaId(), headerToTest.schemaId());
        assertEquals(knownBinaryHeader.blockLength(), headerToTest.blockLength());
    }

    private MutableDirectBuffer loadKnownSingleSidedQuoteBinary() {
        return null;
    }
}
