package com.bht.md.messages.encodehelpers;

import com.bht.md.messages.*;
import org.agrona.MutableDirectBuffer;

public class SingleSidedQuoteEncodeHelper {

    public void encodeSingleSidedQuote(
            SingleSidedQuoteEncoder msgEncoder,
            MutableDirectBuffer buffer,
            MessageHeaderEncoder headerEnc,
            long time,
            short symb,
            MarketState marketstate_,
            QuoteCondition quotecondition_,
            long quote_price,
            long quote_qty,
            QuoteSide quote_side) {
        msgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        msgEncoder.time(time);
        msgEncoder.symb(symb);
        msgEncoder.marketState(marketstate_);
        msgEncoder.quoteCondition(quotecondition_);
        msgEncoder.quote().price(quote_price);
        msgEncoder.quote().qty(quote_qty);
        msgEncoder.quote().side(quote_side);
    }
}
