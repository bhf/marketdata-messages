package com.bht.md.messages.encodehelpers;

import com.bht.md.messages.MarketState;
import com.bht.md.messages.MessageHeaderEncoder;
import com.bht.md.messages.MultiSymbolMassBidsUpdateEncoder;
import com.bht.md.messages.QuoteCondition;
import org.agrona.MutableDirectBuffer;

public class MultiSymbolMassBidsUpdateEncodeHelper {

    public void encodeMultiSymbolMassBidsUpdate(
            MultiSymbolMassBidsUpdateEncoder msgEncoder,
            MutableDirectBuffer buffer,
            MessageHeaderEncoder headerEnc,
            long time,
            MarketState marketstate_,
            QuoteCondition quotecondition_,
            short quoteCount,
            short quotesRemaining,
            short bidQuote1_symb,
            long bidQuote1_price,
            long bidQuote1_qty,
            short bidQuote2_symb,
            long bidQuote2_price,
            long bidQuote2_qty,
            short bidQuote3_symb,
            long bidQuote3_price,
            long bidQuote3_qty,
            short bidQuote4_symb,
            long bidQuote4_price,
            long bidQuote4_qty,
            short bidQuote5_symb,
            long bidQuote5_price,
            long bidQuote5_qty,
            short bidQuote6_symb,
            long bidQuote6_price,
            long bidQuote6_qty,
            short bidQuote7_symb,
            long bidQuote7_price,
            long bidQuote7_qty,
            short bidQuote8_symb,
            long bidQuote8_price,
            long bidQuote8_qty,
            short bidQuote9_symb,
            long bidQuote9_price,
            long bidQuote9_qty,
            short bidQuote10_symb,
            long bidQuote10_price,
            long bidQuote10_qty) {
        msgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        msgEncoder.time(time);
        msgEncoder.marketState(marketstate_);
        msgEncoder.quoteCondition(quotecondition_);
        msgEncoder.quoteCount(quoteCount);
        msgEncoder.quotesRemaining(quotesRemaining);
        msgEncoder.bidQuote1().symb(bidQuote1_symb);
        msgEncoder.bidQuote1().price(bidQuote1_price);
        msgEncoder.bidQuote1().qty(bidQuote1_qty);
        msgEncoder.bidQuote2().symb(bidQuote2_symb);
        msgEncoder.bidQuote2().price(bidQuote2_price);
        msgEncoder.bidQuote2().qty(bidQuote2_qty);
        msgEncoder.bidQuote3().symb(bidQuote3_symb);
        msgEncoder.bidQuote3().price(bidQuote3_price);
        msgEncoder.bidQuote3().qty(bidQuote3_qty);
        msgEncoder.bidQuote4().symb(bidQuote4_symb);
        msgEncoder.bidQuote4().price(bidQuote4_price);
        msgEncoder.bidQuote4().qty(bidQuote4_qty);
        msgEncoder.bidQuote5().symb(bidQuote5_symb);
        msgEncoder.bidQuote5().price(bidQuote5_price);
        msgEncoder.bidQuote5().qty(bidQuote5_qty);
        msgEncoder.bidQuote6().symb(bidQuote6_symb);
        msgEncoder.bidQuote6().price(bidQuote6_price);
        msgEncoder.bidQuote6().qty(bidQuote6_qty);
        msgEncoder.bidQuote7().symb(bidQuote7_symb);
        msgEncoder.bidQuote7().price(bidQuote7_price);
        msgEncoder.bidQuote7().qty(bidQuote7_qty);
        msgEncoder.bidQuote8().symb(bidQuote8_symb);
        msgEncoder.bidQuote8().price(bidQuote8_price);
        msgEncoder.bidQuote8().qty(bidQuote8_qty);
        msgEncoder.bidQuote9().symb(bidQuote9_symb);
        msgEncoder.bidQuote9().price(bidQuote9_price);
        msgEncoder.bidQuote9().qty(bidQuote9_qty);
        msgEncoder.bidQuote10().symb(bidQuote10_symb);
        msgEncoder.bidQuote10().price(bidQuote10_price);
        msgEncoder.bidQuote10().qty(bidQuote10_qty);
    }
}
