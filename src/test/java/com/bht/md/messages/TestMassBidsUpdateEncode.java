package com.bht.md.messages;

import com.bht.md.messages.encodehelpers.MassBidsUpdateEncodeHelper;
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
 * // TODO: Implement test for MassBidsUpdate
 **/
public class TestMassBidsUpdateEncode {

    MassBidsUpdateEncoder encoder = new MassBidsUpdateEncoder();
    MassBidsUpdateEncodeHelper encodeHelper = new MassBidsUpdateEncodeHelper();
    MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    ByteBuffer directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(512, 8);
    MutableDirectBuffer buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);

    final long time = 0;
    final short symb = 0;
    final MarketState marketstate_ = null;
    final QuoteCondition quotecondition_ = null;
    final short quoteCount = 0;
    final short quotesRemaining = 0;
    final long bidQuote1_price = 0;
    final long bidQuote1_qty = 0;
    final long bidQuote2_price = 0;
    final long bidQuote2_qty = 0;
    final long bidQuote3_price = 0;
    final long bidQuote3_qty = 0;
    final long bidQuote4_price = 0;
    final long bidQuote4_qty = 0;
    final long bidQuote5_price = 0;
    final long bidQuote5_qty = 0;
    final long bidQuote6_price = 0;
    final long bidQuote6_qty = 0;
    final long bidQuote7_price = 0;
    final long bidQuote7_qty = 0;
    final long bidQuote8_price = 0;
    final long bidQuote8_qty = 0;
    final long bidQuote9_price = 0;
    final long bidQuote9_qty = 0;
    final long bidQuote10_price = 0;
    final long bidQuote10_qty = 0;

    /**
     * Initialise variables here
     */
    @BeforeEach
    public void init() {}

    /**
     * Test encoding against constant known values
     */
    @Test
    @Tags({@Tag("ConstantValueEncodeDecode"), @Tag("MassBidsUpdate")})
    public void testEncodeUsingConstantValues() {}

    /**
     * Test encoding with randomly generated values
     */
    @Test
    @Tags({@Tag("RandomValueEncodeDecode"), @Tag("MassBidsUpdate")})
    public void testEncodeWithRandomValues() {}

    /**
     * Generate arguments that represent extreme values
     *
     * @return
     */
    private static Stream<Arguments> extremeMassBidsUpdateData() {
        return Stream.of(Arguments.of());
    }

    @ParameterizedTest
    @MethodSource("extremeMassBidsUpdateData")
    @Tags({@Tag("ParamedTests"), @Tag("MassBidsUpdate"), @Tag("ExtremeValueEncodeDecode")})
    public void testEncodeExtremeValues() {}
}
