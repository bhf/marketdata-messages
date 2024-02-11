package com.bht.md.messages.encodehelpers;

import com.bht.md.messages.MarketState;
import com.bht.md.messages.MessageHeaderEncoder;
import com.bht.md.messages.QuoteCondition;
import com.bht.md.messages.TwoSidedQuoteEncoder;
import org.agrona.MutableDirectBuffer;

public class TwoSidedQuoteEncodeHelper {

    public void encodeTwoSidedQuote(
            TwoSidedQuoteEncoder msgEncoder,
            MutableDirectBuffer buffer,
            MessageHeaderEncoder headerEnc,
            long time,
            short symb,
            MarketState marketstate_,
            QuoteCondition quotecondition_,
            long bidQuote_price,
            long bidQuote_qty,
            long askQuote_price,
            long askQuote_qty) {
        msgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        msgEncoder.time(time);
        msgEncoder.symb(symb);
        msgEncoder.marketState(marketstate_);
        msgEncoder.quoteCondition(quotecondition_);
        msgEncoder.bidQuote().price(bidQuote_price);
        msgEncoder.bidQuote().qty(bidQuote_qty);
        msgEncoder.askQuote().price(askQuote_price);
        msgEncoder.askQuote().qty(askQuote_qty);
    }
}
