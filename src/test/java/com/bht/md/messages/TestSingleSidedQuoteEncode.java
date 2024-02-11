package com.bht.md.messages;

import com.bht.md.messages.encodehelpers.SingleSidedQuoteEncodeHelper;
import java.nio.ByteBuffer;
import java.util.stream.Stream;
import org.agrona.MutableDirectBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Generated test template from message spec
 * // TODO: Implement test for SingleSidedQuote
 **/
public class TestSingleSidedQuoteEncode {

    SingleSidedQuoteEncoder encoder = new SingleSidedQuoteEncoder();
    SingleSidedQuoteEncodeHelper encodeHelper = new SingleSidedQuoteEncodeHelper();
    MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    ByteBuffer directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(512, 8);
    MutableDirectBuffer buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);

    final long time = 0;
    final short symb = 0;
    final MarketState marketstate_ = null;
    final QuoteCondition quotecondition_ = null;
    final long quote_price = 0;
    final long quote_qty = 0;
    final QuoteSide quote_side = null;

    /**
     * Initialise variables here
     */
    @BeforeEach
    public void init() {}

    /**
     * Test encoding against constant known values
     */
    @Test
    @Tags({@Tag("ConstantValueEncodeDecode"), @Tag("SingleSidedQuote")})
    public void testEncodeUsingConstantValues() {}

    /**
     * Test encoding with randomly generated values
     */
    @Test
    @Tags({@Tag("RandomValueEncodeDecode"), @Tag("SingleSidedQuote")})
    public void testEncodeWithRandomValues() {}

    /**
     * Generate arguments that represent extreme values
     *
     * @return
     */
    private static Stream<Arguments> extremeSingleSidedQuoteData() {
        return Stream.of(Arguments.of());
    }

    @ParameterizedTest
    @MethodSource("extremeSingleSidedQuoteData")
    @Tags({@Tag("ParamedTests"), @Tag("SingleSidedQuote"), @Tag("ExtremeValueEncodeDecode")})
    public void testEncodeExtremeValues() {}
}
