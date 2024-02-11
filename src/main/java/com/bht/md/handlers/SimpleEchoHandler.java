package com.bht.md.handlers;

import com.bht.md.aeron.AeronPublisher;
import com.bht.md.messages.*;
import io.aeron.logbuffer.BufferClaim;
import java.nio.ByteBuffer;
import org.agrona.BufferUtil;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Echo back some messages over an Aeron Publisher, pass back the original timestamp
 */
public class SimpleEchoHandler extends AbstractSBEDecodingHandler {

    final Logger logger = LoggerFactory.getLogger(SimpleEchoHandler.class);
    final AeronPublisher echoBackPublisher;
    final MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    final SingleSidedQuoteEncoder singleSidedQuoteEncoder = new SingleSidedQuoteEncoder();
    final MassBidsUpdateEncoder massUpdateEnc = new MassBidsUpdateEncoder();
    final GroupedSidedUpdateEncoder rgEncoder = new GroupedSidedUpdateEncoder();
    final ByteBuffer directAlignedBuffer = BufferUtil.allocateDirectAligned(512, 8);
    MutableDirectBuffer buffer = new UnsafeBuffer(directAlignedBuffer);

    public SimpleEchoHandler(AeronPublisher echoBackPublisher, String subscriberID) {
        this.echoBackPublisher = echoBackPublisher;
    }

    @Override
    protected boolean isValid(int tid, int schemaID, int version) {
        return true;
    }

    @Override
    public void handleSingleSidedQuote(SingleSidedQuoteDecoder msgDecoder, int streamId, int sessionId) {

        if (echoBackPublisher.isExclusivePub()) {
            BufferClaim claim = echoBackPublisher.getPublishBuffer(
                    singleSidedQuoteEncoder.sbeBlockLength() + MessageHeaderEncoder.ENCODED_LENGTH);
            buffer = claim.buffer();
            int offset = claim.offset();
            singleSidedQuoteEncoder.wrapAndApplyHeader(buffer, offset, headerEnc);

            singleSidedQuoteEncoder.time(msgDecoder.time());
            short v = 1;
            singleSidedQuoteEncoder.symb(v);
            singleSidedQuoteEncoder.marketState(MarketState.CONTINUOUS_TRADING_MODE);
            singleSidedQuoteEncoder.quoteCondition(QuoteCondition.Direct);
            singleSidedQuoteEncoder.quote().price(321);
            singleSidedQuoteEncoder.quote().qty(11);
            singleSidedQuoteEncoder.quote().side(QuoteSide.Ask);
            echoBackPublisher.doExclusivePublish();
        }
    }

    @Override
    public void handleTwoSidedQuote(TwoSidedQuoteDecoder msgDecoder, int streamId, int sessionId) {}

    @Override
    public void handleMassBidsUpdate(MassBidsUpdateDecoder msgDecoder, int streamId, int sessionId) {

        if (echoBackPublisher.isExclusivePub()) {
            BufferClaim claim = echoBackPublisher.getPublishBuffer(
                    massUpdateEnc.sbeBlockLength() + MessageHeaderEncoder.ENCODED_LENGTH);
            buffer = claim.buffer();
            int offset = claim.offset();
            massUpdateEnc.wrapAndApplyHeader(buffer, offset, headerEnc);
            massUpdateEnc.time(msgDecoder.time());

            short v = 1;
            massUpdateEnc.symb(v);
            massUpdateEnc.marketState(MarketState.CONTINUOUS_TRADING_MODE);
            massUpdateEnc.quoteCondition(QuoteCondition.Direct);
            massUpdateEnc.quoteCount(v);
            massUpdateEnc.quotesRemaining(v);

            massUpdateEnc.bidQuote1().qty(100);
            massUpdateEnc.bidQuote1().price(123);

            massUpdateEnc.bidQuote2().qty(101);
            massUpdateEnc.bidQuote2().price(123);
            massUpdateEnc.bidQuote3().qty(100);
            massUpdateEnc.bidQuote3().price(123);
            massUpdateEnc.bidQuote4().qty(100);
            massUpdateEnc.bidQuote4().price(123);
            massUpdateEnc.bidQuote5().qty(100);
            massUpdateEnc.bidQuote5().price(123);
            massUpdateEnc.bidQuote6().qty(100);
            massUpdateEnc.bidQuote6().price(123);
            massUpdateEnc.bidQuote7().qty(100);
            massUpdateEnc.bidQuote7().price(123);
            massUpdateEnc.bidQuote8().qty(100);
            massUpdateEnc.bidQuote8().price(123);
            massUpdateEnc.bidQuote9().qty(100);
            massUpdateEnc.bidQuote9().price(123);
            massUpdateEnc.bidQuote10().qty(100);
            massUpdateEnc.bidQuote10().price(123);

            echoBackPublisher.doExclusivePublish();
        }
    }

    @Override
    public void handleMassAsksUpdate(MassAsksUpdateDecoder msgDecoder, int streamId, int sessionId) {}

    @Override
    public void handleMassSidedUpdate(MassSidedUpdateDecoder msgDecoder, int streamId, int sessionId) {}

    @Override
    public void handleGroupedSidedUpdate(GroupedSidedUpdateDecoder msgDecoder, int streamId, int sessionId) {

        if (echoBackPublisher.isExclusivePub()) {
            int rgSize = 10 * (GroupedSidedUpdateEncoder.QuoteGroupEncoder.sbeBlockLength());
            int sz = rgSize
                    + rgEncoder.sbeBlockLength()
                    + MessageHeaderEncoder.ENCODED_LENGTH
                    + GroupedSidedUpdateEncoder.QuoteGroupEncoder.sbeHeaderSize();

            BufferClaim claim = echoBackPublisher.getPublishBuffer(sz);
            buffer = claim.buffer();
            int offset = claim.offset();
            rgEncoder.wrapAndApplyHeader(buffer, offset, headerEnc);
            rgEncoder.time(msgDecoder.time());

            short v = 1;
            rgEncoder.symb(v);
            rgEncoder.marketState(MarketState.CONTINUOUS_TRADING_MODE);
            rgEncoder.quoteCondition(QuoteCondition.Direct);
            rgEncoder.quotesRemaining(v);

            GroupedSidedUpdateEncoder.QuoteGroupEncoder quoteGroupEncoder = rgEncoder.quoteGroupCount(10);
            for (int i = 0; i < 10; i++) {
                quoteGroupEncoder.next();
                quoteGroupEncoder.sidedQuote().price(10);
                quoteGroupEncoder.sidedQuote().qty(10);
                quoteGroupEncoder.sidedQuote().side(QuoteSide.Ask);
            }
            echoBackPublisher.doExclusivePublish();
        }
    }

    @Override
    public void handleMultiSymbolMassBidsUpdate(
            MultiSymbolMassBidsUpdateDecoder msgDecoder, int streamId, int sessionId) {}

    @Override
    public void handleMultiSymbolMassAsksUpdate(
            MultiSymbolMassAsksUpdateDecoder msgDecoder, int streamId, int sessionId) {}

    @Override
    public void handleMultiSymbolMassSidedUpdate(
            MultiSymbolMassSidedUpdateDecoder msgDecoder, int streamId, int sessionId) {}

    @Override
    public void handleMarketState(MarketStateDecoder msgDecoder, int streamId, int sessionId) {}
}
