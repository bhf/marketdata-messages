package com.bht.md.messages.encodehelpers;

import com.bht.md.messages.MarketState;
import com.bht.md.messages.MassBidsUpdateEncoder;
import com.bht.md.messages.MessageHeaderEncoder;
import com.bht.md.messages.QuoteCondition;
import org.agrona.MutableDirectBuffer;

public class MassBidsUpdateEncodeHelper {

    public void encodeMassBidsUpdate(
            MassBidsUpdateEncoder msgEncoder,
            MutableDirectBuffer buffer,
            MessageHeaderEncoder headerEnc,
            long time,
            short symb,
            MarketState marketstate_,
            QuoteCondition quotecondition_,
            short quoteCount,
            short quotesRemaining,
            long bidQuote1_price,
            long bidQuote1_qty,
            long bidQuote2_price,
            long bidQuote2_qty,
            long bidQuote3_price,
            long bidQuote3_qty,
            long bidQuote4_price,
            long bidQuote4_qty,
            long bidQuote5_price,
            long bidQuote5_qty,
            long bidQuote6_price,
            long bidQuote6_qty,
            long bidQuote7_price,
            long bidQuote7_qty,
            long bidQuote8_price,
            long bidQuote8_qty,
            long bidQuote9_price,
            long bidQuote9_qty,
            long bidQuote10_price,
            long bidQuote10_qty) {
        msgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        msgEncoder.time(time);
        msgEncoder.symb(symb);
        msgEncoder.marketState(marketstate_);
        msgEncoder.quoteCondition(quotecondition_);
        msgEncoder.quoteCount(quoteCount);
        msgEncoder.quotesRemaining(quotesRemaining);
        msgEncoder.bidQuote1().price(bidQuote1_price);
        msgEncoder.bidQuote1().qty(bidQuote1_qty);
        msgEncoder.bidQuote2().price(bidQuote2_price);
        msgEncoder.bidQuote2().qty(bidQuote2_qty);
        msgEncoder.bidQuote3().price(bidQuote3_price);
        msgEncoder.bidQuote3().qty(bidQuote3_qty);
        msgEncoder.bidQuote4().price(bidQuote4_price);
        msgEncoder.bidQuote4().qty(bidQuote4_qty);
        msgEncoder.bidQuote5().price(bidQuote5_price);
        msgEncoder.bidQuote5().qty(bidQuote5_qty);
        msgEncoder.bidQuote6().price(bidQuote6_price);
        msgEncoder.bidQuote6().qty(bidQuote6_qty);
        msgEncoder.bidQuote7().price(bidQuote7_price);
        msgEncoder.bidQuote7().qty(bidQuote7_qty);
        msgEncoder.bidQuote8().price(bidQuote8_price);
        msgEncoder.bidQuote8().qty(bidQuote8_qty);
        msgEncoder.bidQuote9().price(bidQuote9_price);
        msgEncoder.bidQuote9().qty(bidQuote9_qty);
        msgEncoder.bidQuote10().price(bidQuote10_price);
        msgEncoder.bidQuote10().qty(bidQuote10_qty);
    }
}
