package com.bht.md.messages;

import com.bht.md.messages.encodehelpers.MultiSymbolMassSidedUpdateEncodeHelper;
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
 * // TODO: Implement test for MultiSymbolMassSidedUpdate
 **/
public class TestMultiSymbolMassSidedUpdateEncode {

    MultiSymbolMassSidedUpdateEncoder encoder = new MultiSymbolMassSidedUpdateEncoder();
    MultiSymbolMassSidedUpdateEncodeHelper encodeHelper = new MultiSymbolMassSidedUpdateEncodeHelper();
    MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    ByteBuffer directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(512, 8);
    MutableDirectBuffer buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);

    final long time = 0;
    final MarketState marketstate_ = null;
    final QuoteCondition quotecondition_ = null;
    final short quoteCount = 0;
    final short quotesRemaining = 0;
    final short sidedQuote1_symb = 0;
    final long sidedQuote1_price = 0;
    final long sidedQuote1_qty = 0;
    final QuoteSide sidedQuote1_side = null;
    final short sidedQuote2_symb = 0;
    final long sidedQuote2_price = 0;
    final long sidedQuote2_qty = 0;
    final QuoteSide sidedQuote2_side = null;
    final short sidedQuote3_symb = 0;
    final long sidedQuote3_price = 0;
    final long sidedQuote3_qty = 0;
    final QuoteSide sidedQuote3_side = null;
    final short sidedQuote4_symb = 0;
    final long sidedQuote4_price = 0;
    final long sidedQuote4_qty = 0;
    final QuoteSide sidedQuote4_side = null;
    final short sidedQuote5_symb = 0;
    final long sidedQuote5_price = 0;
    final long sidedQuote5_qty = 0;
    final QuoteSide sidedQuote5_side = null;
    final short sidedQuote6_symb = 0;
    final long sidedQuote6_price = 0;
    final long sidedQuote6_qty = 0;
    final QuoteSide sidedQuote6_side = null;
    final short sidedQuote7_symb = 0;
    final long sidedQuote7_price = 0;
    final long sidedQuote7_qty = 0;
    final QuoteSide sidedQuote7_side = null;
    final short sidedQuote8_symb = 0;
    final long sidedQuote8_price = 0;
    final long sidedQuote8_qty = 0;
    final QuoteSide sidedQuote8_side = null;
    final short sidedQuote9_symb = 0;
    final long sidedQuote9_price = 0;
    final long sidedQuote9_qty = 0;
    final QuoteSide sidedQuote9_side = null;
    final short sidedQuote10_symb = 0;
    final long sidedQuote10_price = 0;
    final long sidedQuote10_qty = 0;
    final QuoteSide sidedQuote10_side = null;

    /**
     * Initialise variables here
     */
    @BeforeEach
    public void init() {}

    /**
     * Test encoding against constant known values
     */
    @Test
    @Tags({@Tag("ConstantValueEncodeDecode"), @Tag("MultiSymbolMassSidedUpdate")})
    public void testEncodeUsingConstantValues() {}

    /**
     * Test encoding with randomly generated values
     */
    @Test
    @Tags({@Tag("RandomValueEncodeDecode"), @Tag("MultiSymbolMassSidedUpdate")})
    public void testEncodeWithRandomValues() {}

    /**
     * Generate arguments that represent extreme values
     *
     * @return
     */
    private static Stream<Arguments> extremeMultiSymbolMassSidedUpdateData() {
        return Stream.of(Arguments.of());
    }

    @ParameterizedTest
    @MethodSource("extremeMultiSymbolMassSidedUpdateData")
    @Tags({@Tag("ParamedTests"), @Tag("MultiSymbolMassSidedUpdate"), @Tag("ExtremeValueEncodeDecode")})
    public void testEncodeExtremeValues() {}
}
