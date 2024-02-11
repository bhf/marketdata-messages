package com.bhf.encodervalidation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bht.md.messages.*;
import com.bht.md.messages.encodehelpers.SingleSidedQuoteEncodeHelper;
import java.nio.ByteBuffer;
import java.util.Random;
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
 *Exploring different ways of unit testing a message encoing process:
 * 1. Testing against hard coded values
 * 2. Testing against randomly generated values
 * 3. Testing against extreme values
 * 4. Testing against a stream of arguments to test a few combinations out
 *
 * Cases 2 and 3 can suffer from examples where the SBE type doesn't match
 * the Java type range, but this can be solved using explicit fields for max and min
 * values somewhere centrally. It is also not clear whether this kind of check
 * should be done at this level e.g. if as part of your encoding chain you have
 * validation steps on the values of the fields being encoded then it would be
 * better to test that field ranges are validated in a seperate group of tests
 * which test the validation process explicitly
 *
 * Case 4 offers more value range coverage vs Case 1 (hard coded values)
 * and may be preferable to random values (deterministic values being tested against)
 *
 * Please note that testing the order of calls is correct is handled
 * by tests on the *EncodeHelper which is where the actual SBE message ordering becomes
 * important - this creates a seperation of concerns. You can see the examples
 * of call ordering testing in ExploringCallOrderingTestStyles.
 */
public class EncoderValueUnitTestStyles {

    public static final int CAPACITY = 512;
    public static final int ALIGNMENT = 8;
    SingleSidedQuoteEncoder singleSidedQuoteEncoder = new SingleSidedQuoteEncoder();
    SingleSidedQuoteEncodeHelper singleSidedQuoteEncodeHelper = new SingleSidedQuoteEncodeHelper();
    MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    ByteBuffer directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(512, 8);
    MutableDirectBuffer buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);

    /**
     * Reinitialise the encoder and encode helper
     * We want to test every run with fresh state
     */
    @BeforeEach
    public void init() {
        singleSidedQuoteEncoder = new SingleSidedQuoteEncoder();
        singleSidedQuoteEncodeHelper = new SingleSidedQuoteEncodeHelper();
        headerEnc = new MessageHeaderEncoder();
        directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(CAPACITY, ALIGNMENT);
        buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);
    }

    /**
     *Test encoding against constant known values that are hard coded
     */
    @Test
    @Tags({@Tag("ConstantValueEncodeDecode"), @Tag("SingleSidedQuote")})
    public void testEncodeUsingConstantValues() {

        final long timeValue = 12923939;
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

        SingleSidedQuoteDecoder dec = new SingleSidedQuoteDecoder();
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        dec.wrapAndApplyHeader(buffer, 0, headerDecoder);

        assertEquals(dec.time(), timeValue);
        assertEquals(dec.symb(), symbolId);
        assertEquals(dec.marketState(), marketState);
        assertEquals(dec.quoteCondition(), quoteCondition);
        assertEquals(dec.quote().price(), price);
        assertEquals(dec.quote().qty(), qty);
        assertEquals(dec.quote().side(), quoteSide);
    }

    /**
     * Test encoding with randomly generated values
     */
    @Test
    @Tags({@Tag("RandomValueEncodeDecode"), @Tag("SingleSidedQuote")})
    public void testEncodeWithRandomValues() {

        Random rnd = new Random();
        QuoteCondition quoteCondition = rnd.nextBoolean() ? QuoteCondition.Direct : QuoteCondition.Implied;
        long time = rnd.nextLong();
        long px = rnd.nextLong();
        long qty = rnd.nextLong();
        int states = MarketState.values().length;
        int sIndex = rnd.nextInt(states - 1);
        MarketState marketState = MarketState.values()[sIndex];
        short symb = (short) (rnd.nextBoolean() ? 1 : 2);
        QuoteSide isAsk = rnd.nextBoolean() ? QuoteSide.Ask : QuoteSide.Bid;

        singleSidedQuoteEncodeHelper.encodeSingleSidedQuote(
                singleSidedQuoteEncoder, buffer, headerEnc, time, symb, marketState, quoteCondition, px, qty, isAsk);

        SingleSidedQuoteDecoder dec = new SingleSidedQuoteDecoder();
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        dec.wrapAndApplyHeader(buffer, 0, headerDecoder);

        assertEquals(dec.time(), time);
        assertEquals(dec.symb(), symb);
        assertEquals(dec.marketState(), marketState);
        assertEquals(dec.quoteCondition(), quoteCondition);
        assertEquals(dec.quote().price(), px);
        assertEquals(dec.quote().qty(), qty);
        assertEquals(dec.quote().side(), isAsk);
    }

    /**
     * Generate arguments that represent extreme values Note that the max value for symbol is 254
     * (uint8) not that of the native Java short (Short.MAX_VALUE)
     *
     * @return
     */
    private static Stream<Arguments> extremeSingleSidedQuoteData() {
        long timeMax = Long.MAX_VALUE;
        short symbolMax = 254;
        long priceMax = Long.MAX_VALUE;
        long qtyMax = Long.MAX_VALUE;

        long timeMin = Long.MIN_VALUE;
        short symbolMin = 0;
        long priceMin = Long.MIN_VALUE;
        long qtyMin = Long.MIN_VALUE;

        MarketState marketState = MarketState.CONTINUOUS_TRADING_MODE;
        QuoteCondition quoteCond = QuoteCondition.Direct;
        QuoteSide isAsk = QuoteSide.Bid;

        return Stream.of(
                Arguments.of(timeMax, symbolMax, marketState, quoteCond, priceMax, qtyMax, isAsk),
                Arguments.of(timeMin, symbolMin, marketState, quoteCond, priceMin, qtyMin, isAsk));
    }

    /**
     * Test extreme values of fields for edge cases
     * @param timeValue_
     * @param symbolId_
     * @param marketState_
     * @param quoteCondition_
     * @param price_
     * @param qty_
     * @param isAsk_
     */
    @ParameterizedTest
    @MethodSource("extremeSingleSidedQuoteData")
    @Tags({@Tag("ParamedTests"), @Tag("SingleSidedQuote"), @Tag("ExtremeValueEncodeDecode")})
    public void testEncodeExtremeValues(
            long timeValue_,
            short symbolId_,
            MarketState marketState_,
            QuoteCondition quoteCondition_,
            long price_,
            long qty_,
            QuoteSide isAsk_) {
        singleSidedQuoteEncodeHelper.encodeSingleSidedQuote(
                singleSidedQuoteEncoder,
                buffer,
                headerEnc,
                timeValue_,
                symbolId_,
                marketState_,
                quoteCondition_,
                price_,
                qty_,
                isAsk_);

        SingleSidedQuoteDecoder dec = new SingleSidedQuoteDecoder();
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        dec.wrapAndApplyHeader(buffer, 0, headerDecoder);

        assertEquals(dec.time(), timeValue_);
        assertEquals(dec.symb(), symbolId_);
        assertEquals(dec.marketState(), marketState_);
        assertEquals(dec.quoteCondition(), quoteCondition_);
        assertEquals(dec.quote().price(), price_);
        assertEquals(dec.quote().qty(), qty_);
        assertEquals(dec.quote().side(), isAsk_);
    }

    /**
     * Generate a few explicit samples vs. using hard coded values
     *
     * @return
     */
    private static Stream<Arguments> explicitSamples() {
        return Stream.of(
                Arguments.of(
                        123,
                        (short) 1,
                        MarketState.CONTINUOUS_TRADING_MODE,
                        QuoteCondition.Direct,
                        34,
                        2,
                        QuoteSide.Bid),
                Arguments.of(
                        123,
                        (short) 1,
                        MarketState.CONTINUOUS_TRADING_MODE,
                        QuoteCondition.NonFirm,
                        34,
                        2,
                        QuoteSide.Ask),
                Arguments.of(
                        123456, (short) 2, MarketState.OPENING_MODE, QuoteCondition.Implied, 34, 2, QuoteSide.Bid));
    }

    @ParameterizedTest
    @MethodSource("explicitSamples")
    @Tags({@Tag("ParamedTests"), @Tag("SingleSidedQuote"), @Tag("ExplicitSampleValues")})
    public void testExplicitSampleValues(
            long timeValue_,
            short symbolId_,
            MarketState marketState_,
            QuoteCondition quoteCondition_,
            long price_,
            long qty_,
            QuoteSide isAsk_) {
        singleSidedQuoteEncodeHelper.encodeSingleSidedQuote(
                singleSidedQuoteEncoder,
                buffer,
                headerEnc,
                timeValue_,
                symbolId_,
                marketState_,
                quoteCondition_,
                price_,
                qty_,
                isAsk_);

        SingleSidedQuoteDecoder dec = new SingleSidedQuoteDecoder();
        MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        dec.wrapAndApplyHeader(buffer, 0, headerDecoder);

        assertEquals(dec.time(), timeValue_);
        assertEquals(dec.symb(), symbolId_);
        assertEquals(dec.marketState(), marketState_);
        assertEquals(dec.quoteCondition(), quoteCondition_);
        assertEquals(dec.quote().price(), price_);
        assertEquals(dec.quote().qty(), qty_);
        assertEquals(dec.quote().side(), isAsk_);
    }
}
