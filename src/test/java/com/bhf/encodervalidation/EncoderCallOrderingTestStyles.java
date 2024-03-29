package com.bhf.encodervalidation;

import com.bht.md.messages.*;
import com.bht.md.messages.encodehelpers.SingleSidedQuoteEncodeHelper;
import java.nio.ByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

/** Explore different styles of testing for call ordering for SBE encode helpers
 * Please note: No testing pipeline is infallible, but we try and create
 * layers of safety nets wherever we can
 *
 * Alternatives to mockito are using anonymous classes
 * with a collection for recording the ordering
 * */
public class EncoderCallOrderingTestStyles {

    public static final int CAPACITY = 512;
    public static final int ALIGNMENT = 8;
    SingleSidedQuoteEncodeHelper encodeHelper = new SingleSidedQuoteEncodeHelper();

    ByteBuffer directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(512, 8);
    MutableDirectBuffer buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);

    /** Reinitialise the encoder and encode helper We want to test every run with fresh state */
    @BeforeEach
    public void init() {
        encodeHelper = new SingleSidedQuoteEncodeHelper();
        directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(CAPACITY, ALIGNMENT);
        buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);
    }

    /** Test encoding against constant known values that are hard coded */
    @Test
    @Tags({@Tag("EncoderCallOrdering"), @Tag("SingleSidedQuote")})
    public void testEncodeCallOrdering() {
        MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();

        // You have to mock each of the sub encoders of the top level encoder
        SidedQuoteEncoder quoteEnc = Mockito.mock(SidedQuoteEncoder.class);
        SingleSidedQuoteEncoder encoder = Mockito.mock(SingleSidedQuoteEncoder.class);

        // make sure the top level encoder will return the mock of each of the sub-encoders
        Mockito.when(encoder.quote()).thenReturn(quoteEnc);

        // Create an InOrder with all the mocks created - top level encoder and each sub-encoder
        InOrder order = Mockito.inOrder(encoder, quoteEnc);

        // choose your preferred value injection method, testing different
        // combinations of values may not be necessary, but it does
        // presume no branching logic in your encoder, so we run the risk
        // of not exercising that logic
        // (whether or not that branching logic is by accident or design)

        final long timeValue = 0;
        final short symbolId = 1;
        final QuoteCondition quoteCondition = QuoteCondition.Direct;
        final MarketState marketState = MarketState.CONTINUOUS_TRADING_MODE;
        final long price = 123;
        final long qty = 55;
        final QuoteSide quoteSide = QuoteSide.Ask;

        encodeHelper.encodeSingleSidedQuote(
                encoder, buffer, headerEnc, timeValue, symbolId, marketState, quoteCondition, price, qty, quoteSide);

        // Verify the correct order of calls - the problem with this
        // comes if you write the unit test in the same way that you
        // write the encode helper i.e. both have the same ordering, but
        // the ordering is incorrect. This is where the standard tests
        // from ExploringEncoderUnitTestStyles come in useful. Other
        // strategies for solving this risk are to have one person write
        // the unit tests and another person the encoder.

        // Alternatively if the encoder, encode helper and unit test
        // are all autogenerated, there is still the risk that the way
        // the codegenerator is interpreting the fields and messages
        // is wrong. The standard value check type tests
        // (like those in ExploringEncoderUnitTestStyles) may help in this
        // case also, but if those are also autogenerated, then
        // they may also suffer from the same biases or issues.

        // A third solution is to conduct the tests against known samples of
        // raw binary which represent known SBE messages. See "ExploringRawMessageTestStyles".

        order.verify(encoder).wrapAndApplyHeader(buffer, 0, headerEnc);
        order.verify(encoder).time(timeValue);
        order.verify(encoder).symb(symbolId);
        order.verify(encoder).marketState(marketState);
        order.verify(encoder).quoteCondition(quoteCondition);
        order.verify(quoteEnc).price(price);
        order.verify(quoteEnc).qty(qty);
        order.verify(quoteEnc).side(quoteSide);

        // at the end, make sure nothing else is called
        order.verifyNoMoreInteractions();
    }
}
