package com.bht.md.samples.roundtrip;

import com.bht.md.aeron.AeronPublisher;
import com.bht.md.aeron.AeronSubscriber;
import com.bht.md.messages.*;
import io.aeron.logbuffer.BufferClaim;
import io.aeron.logbuffer.FragmentHandler;
import java.nio.ByteBuffer;
import org.agrona.BufferUtil;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This thread is responsible for publishing 3 types of messages and also processing the
 * echoed back messages.
 *
 * The three types of messages are
 * 1. Single sided quote - a small message
 * 2. A book update of 10 levels using explicit quotes per level - a large message without repeating groups
 * 3. A book update of 10 levels using repeating groups - a large message using repeating groups
 */
public class EchoReceivingPublisher implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(EchoReceivingPublisher.class);
    int sent = 0;
    private final MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    private final SingleSidedQuoteEncoder singleSidedQuoteEncoder = new SingleSidedQuoteEncoder();
    private final ByteBuffer directAlignedBuffer = BufferUtil.allocateDirectAligned(1024, 8);
    MutableDirectBuffer buffer = new UnsafeBuffer(directAlignedBuffer);
    private final FragmentHandler echoListeningFragmentHandler;
    private final AeronPublisher pub;
    private final AeronSubscriber echoSubscriber;
    private final IdleStrategy idleStrategy;

    public EchoReceivingPublisher(
            FragmentHandler echoListeningFragmentHandler,
            AeronPublisher pub,
            AeronSubscriber echoSubscriber,
            IdleStrategy idleStrategy) {
        this.echoListeningFragmentHandler = echoListeningFragmentHandler;
        this.pub = pub;
        this.echoSubscriber = echoSubscriber;
        this.idleStrategy = idleStrategy;
    }

    @Override
    public void run() {
        while (true) {

            idleStrategy.reset();
            long start = System.nanoTime();

            int rem = sent % 3;

            if (rem == 0) {
                publishSingleSidedQuote(start);
            } else if (rem == 1) {
                publishMassBidUpdate(start);
            } else if (rem == 2) {
                publishMassBidUpdateRG(start);
            }

            sent++;
            echoSubscriber.doWork(echoListeningFragmentHandler);
            idleStrategy.idle();
        }
    }

    private void publishSingleSidedQuote(long start) {
        if (pub.isExclusivePub()) {
            BufferClaim claim = pub.getPublishBuffer(
                    singleSidedQuoteEncoder.sbeBlockLength() + MessageHeaderEncoder.ENCODED_LENGTH);
            buffer = claim.buffer();
            int offset = claim.offset();
            singleSidedQuoteEncoder.wrapAndApplyHeader(buffer, offset, headerEnc);
        } else {
            singleSidedQuoteEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        }

        singleSidedQuoteEncoder.time(start);
        short v = 1;
        singleSidedQuoteEncoder.symb(v);
        singleSidedQuoteEncoder.marketState(MarketState.CONTINUOUS_TRADING_MODE);
        singleSidedQuoteEncoder.quoteCondition(QuoteCondition.Direct);
        singleSidedQuoteEncoder.quote().price(123);
        singleSidedQuoteEncoder.quote().qty(10);
        singleSidedQuoteEncoder.quote().side(QuoteSide.Ask);

        if (!pub.isExclusivePub()) {
            int encodingLengthPlusHeader =
                    singleSidedQuoteEncoder.encodedLength() + MessageHeaderEncoder.ENCODED_LENGTH;
            pub.doNonExclusivePublish(buffer, encodingLengthPlusHeader);
        } else {
            pub.doExclusivePublish();
        }
    }

    final MassBidsUpdateEncoder massBidsUpdateEncoder = new MassBidsUpdateEncoder();

    private void publishMassBidUpdate(long start) {
        if (pub.isExclusivePub()) {
            BufferClaim claim =
                    pub.getPublishBuffer(massBidsUpdateEncoder.sbeBlockLength() + MessageHeaderEncoder.ENCODED_LENGTH);
            buffer = claim.buffer();
            int offset = claim.offset();
            massBidsUpdateEncoder.wrapAndApplyHeader(buffer, offset, headerEnc);
        } else {
            massBidsUpdateEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        }

        massBidsUpdateEncoder.time(start);
        short v = 1;
        massBidsUpdateEncoder.symb(v);
        massBidsUpdateEncoder.marketState(MarketState.CONTINUOUS_TRADING_MODE);
        massBidsUpdateEncoder.quoteCondition(QuoteCondition.Direct);
        massBidsUpdateEncoder.quoteCount(v);
        massBidsUpdateEncoder.quotesRemaining(v);

        massBidsUpdateEncoder.bidQuote1().qty(100);
        massBidsUpdateEncoder.bidQuote1().price(123);

        massBidsUpdateEncoder.bidQuote2().qty(101);
        massBidsUpdateEncoder.bidQuote2().price(123);
        massBidsUpdateEncoder.bidQuote3().qty(100);
        massBidsUpdateEncoder.bidQuote3().price(123);
        massBidsUpdateEncoder.bidQuote4().qty(100);
        massBidsUpdateEncoder.bidQuote4().price(123);
        massBidsUpdateEncoder.bidQuote5().qty(100);
        massBidsUpdateEncoder.bidQuote5().price(123);
        massBidsUpdateEncoder.bidQuote6().qty(100);
        massBidsUpdateEncoder.bidQuote6().price(123);
        massBidsUpdateEncoder.bidQuote7().qty(100);
        massBidsUpdateEncoder.bidQuote7().price(123);
        massBidsUpdateEncoder.bidQuote8().qty(100);
        massBidsUpdateEncoder.bidQuote8().price(123);
        massBidsUpdateEncoder.bidQuote9().qty(100);
        massBidsUpdateEncoder.bidQuote9().price(123);
        massBidsUpdateEncoder.bidQuote10().qty(100);
        massBidsUpdateEncoder.bidQuote10().price(123);

        if (!pub.isExclusivePub()) {
            int encodingLengthPlusHeader = massBidsUpdateEncoder.encodedLength() + MessageHeaderEncoder.ENCODED_LENGTH;
            pub.doNonExclusivePublish(buffer, encodingLengthPlusHeader);
        } else {
            pub.doExclusivePublish();
        }
    }

    final GroupedSidedUpdateEncoder rgEncoder = new GroupedSidedUpdateEncoder();

    private void publishMassBidUpdateRG(long start) {

        if (pub.isExclusivePub()) {
            int rgSize = 10 * (GroupedSidedUpdateEncoder.QuoteGroupEncoder.sbeBlockLength());
            int sz = rgSize
                    + rgEncoder.sbeBlockLength()
                    + MessageHeaderEncoder.ENCODED_LENGTH
                    + GroupedSidedUpdateEncoder.QuoteGroupEncoder.sbeHeaderSize();
            BufferClaim claim = pub.getPublishBuffer(sz);
            buffer = claim.buffer();
            int offset = claim.offset();
            rgEncoder.wrapAndApplyHeader(buffer, offset, headerEnc);
        } else {
            rgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        }

        rgEncoder.time(start);
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

        if (!pub.isExclusivePub()) {
            int encodingLengthPlusHeader = rgEncoder.encodedLength() + MessageHeaderEncoder.ENCODED_LENGTH;
            pub.doNonExclusivePublish(buffer, encodingLengthPlusHeader);
        } else {
            pub.doExclusivePublish();
        }
    }
}
