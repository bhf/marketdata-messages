package com.bht.md.messages;

import com.bht.md.messages.encodehelpers.TwoSidedQuoteEncodeHelper;
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
 * // TODO: Implement test for TwoSidedQuote
 **/
public class TestTwoSidedQuoteEncode {

    TwoSidedQuoteEncoder encoder = new TwoSidedQuoteEncoder();
    TwoSidedQuoteEncodeHelper encodeHelper = new TwoSidedQuoteEncodeHelper();
    MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    ByteBuffer directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(512, 8);
    MutableDirectBuffer buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);

    final long time = 0;
    final short symb = 0;
    final MarketState marketstate_ = null;
    final QuoteCondition quotecondition_ = null;
    final long bidQuote_price = 0;
    final long bidQuote_qty = 0;
    final long askQuote_price = 0;
    final long askQuote_qty = 0;

    /**
     * Initialise variables here
     */
    @BeforeEach
    public void init() {}

    /**
     * Test encoding against constant known values
     */
    @Test
    @Tags({@Tag("ConstantValueEncodeDecode"), @Tag("TwoSidedQuote")})
    public void testEncodeUsingConstantValues() {}

    /**
     * Test encoding with randomly generated values
     */
    @Test
    @Tags({@Tag("RandomValueEncodeDecode"), @Tag("TwoSidedQuote")})
    public void testEncodeWithRandomValues() {}

    /**
     * Generate arguments that represent extreme values
     *
     * @return
     */
    private static Stream<Arguments> extremeTwoSidedQuoteData() {
        return Stream.of(Arguments.of());
    }

    @ParameterizedTest
    @MethodSource("extremeTwoSidedQuoteData")
    @Tags({@Tag("ParamedTests"), @Tag("TwoSidedQuote"), @Tag("ExtremeValueEncodeDecode")})
    public void testEncodeExtremeValues() {}
}
