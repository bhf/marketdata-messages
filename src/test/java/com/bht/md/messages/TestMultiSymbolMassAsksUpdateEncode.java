package com.bht.md.messages;

import com.bht.md.messages.encodehelpers.MultiSymbolMassAsksUpdateEncodeHelper;
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
 * // TODO: Implement test for MultiSymbolMassAsksUpdate
 **/
public class TestMultiSymbolMassAsksUpdateEncode {

    MultiSymbolMassAsksUpdateEncoder encoder = new MultiSymbolMassAsksUpdateEncoder();
    MultiSymbolMassAsksUpdateEncodeHelper encodeHelper = new MultiSymbolMassAsksUpdateEncodeHelper();
    MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    ByteBuffer directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(512, 8);
    MutableDirectBuffer buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);

    final long time = 0;
    final MarketState marketstate_ = null;
    final QuoteCondition quotecondition_ = null;
    final short quoteCount = 0;
    final short quotesRemaining = 0;
    final short askQuote1_symb = 0;
    final long askQuote1_price = 0;
    final long askQuote1_qty = 0;
    final short askQuote2_symb = 0;
    final long askQuote2_price = 0;
    final long askQuote2_qty = 0;
    final short askQuote3_symb = 0;
    final long askQuote3_price = 0;
    final long askQuote3_qty = 0;
    final short askQuote4_symb = 0;
    final long askQuote4_price = 0;
    final long askQuote4_qty = 0;
    final short askQuote5_symb = 0;
    final long askQuote5_price = 0;
    final long askQuote5_qty = 0;
    final short askQuote6_symb = 0;
    final long askQuote6_price = 0;
    final long askQuote6_qty = 0;
    final short askQuote7_symb = 0;
    final long askQuote7_price = 0;
    final long askQuote7_qty = 0;
    final short askQuote8_symb = 0;
    final long askQuote8_price = 0;
    final long askQuote8_qty = 0;
    final short askQuote9_symb = 0;
    final long askQuote9_price = 0;
    final long askQuote9_qty = 0;
    final short askQuote10_symb = 0;
    final long askQuote10_price = 0;
    final long askQuote10_qty = 0;

    /**
     * Initialise variables here
     */
    @BeforeEach
    public void init() {}

    /**
     * Test encoding against constant known values
     */
    @Test
    @Tags({@Tag("ConstantValueEncodeDecode"), @Tag("MultiSymbolMassAsksUpdate")})
    public void testEncodeUsingConstantValues() {}

    /**
     * Test encoding with randomly generated values
     */
    @Test
    @Tags({@Tag("RandomValueEncodeDecode"), @Tag("MultiSymbolMassAsksUpdate")})
    public void testEncodeWithRandomValues() {}

    /**
     * Generate arguments that represent extreme values
     *
     * @return
     */
    private static Stream<Arguments> extremeMultiSymbolMassAsksUpdateData() {
        return Stream.of(Arguments.of());
    }

    @ParameterizedTest
    @MethodSource("extremeMultiSymbolMassAsksUpdateData")
    @Tags({@Tag("ParamedTests"), @Tag("MultiSymbolMassAsksUpdate"), @Tag("ExtremeValueEncodeDecode")})
    public void testEncodeExtremeValues() {}
}
