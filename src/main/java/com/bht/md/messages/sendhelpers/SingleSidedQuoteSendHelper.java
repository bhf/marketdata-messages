package com.bht.md.messages.sendhelpers;

import com.bht.md.aeron.AeronPublisher;
import com.bht.md.messages.*;
import com.bht.md.messages.encodehelpers.SingleSidedQuoteEncodeHelper;
import io.aeron.logbuffer.BufferClaim;
import org.agrona.BufferUtil;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

public class SingleSidedQuoteSendHelper {

    private final AeronPublisher pub;
    private final SingleSidedQuoteEncoder encoder = new SingleSidedQuoteEncoder();
    private final SingleSidedQuoteEncodeHelper encodeHelper = new SingleSidedQuoteEncodeHelper();
    private final MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    MutableDirectBuffer buffer;

    public SingleSidedQuoteSendHelper(AeronPublisher pub) {
        this.pub = pub;

        if (!pub.isExclusivePub()) {
            // create local buffer for sending
            buffer = new UnsafeBuffer(BufferUtil.allocateDirectAligned(512, 8));
        }
    }

    // if publisher is exclusive, get the buffer from the bufferclaim
    // if the publisher is non-exclusive, reuse a locally created buffer

    public void send(
            long time,
            short symb,
            MarketState marketstate_,
            QuoteCondition quotecondition_,
            long quote_price,
            long quote_qty,
            QuoteSide quote_side) {
        MutableDirectBuffer b = null;
        int msgLen = getMsgLength();

        if (pub.isExclusivePub()) {
            BufferClaim publishBuffer = pub.getPublishBuffer(msgLen);
            b = publishBuffer.buffer();
        } else {
            b = buffer;
        }

        encodeHelper.encodeSingleSidedQuote(
                encoder, b, headerEnc, time, symb, marketstate_, quotecondition_, quote_price, quote_qty, quote_side);

        if (pub.isExclusivePub()) {
            pub.doExclusivePublish();
        } else {
            pub.doNonExclusivePublish(b, msgLen);
        }
    }

    private int getMsgLength() {
        return encoder.sbeBlockLength() + MessageHeaderEncoder.ENCODED_LENGTH;
    }
}
