package com.bht.md.messages.encodehelpers;

import com.bht.md.messages.MarketState;
import com.bht.md.messages.MassAsksUpdateEncoder;
import com.bht.md.messages.MessageHeaderEncoder;
import com.bht.md.messages.QuoteCondition;
import org.agrona.MutableDirectBuffer;

public class MassAsksUpdateEncodeHelper {

    public void encodeMassAsksUpdate(
            MassAsksUpdateEncoder msgEncoder,
            MutableDirectBuffer buffer,
            MessageHeaderEncoder headerEnc,
            long time,
            short symb,
            MarketState marketstate_,
            QuoteCondition quotecondition_,
            short quoteCount,
            short quotesRemaining,
            long askQuote1_price,
            long askQuote1_qty,
            long askQuote2_price,
            long askQuote2_qty,
            long askQuote3_price,
            long askQuote3_qty,
            long askQuote4_price,
            long askQuote4_qty,
            long askQuote5_price,
            long askQuote5_qty,
            long askQuote6_price,
            long askQuote6_qty,
            long askQuote7_price,
            long askQuote7_qty,
            long askQuote8_price,
            long askQuote8_qty,
            long askQuote9_price,
            long askQuote9_qty,
            long askQuote10_price,
            long askQuote10_qty) {
        msgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        msgEncoder.time(time);
        msgEncoder.symb(symb);
        msgEncoder.marketState(marketstate_);
        msgEncoder.quoteCondition(quotecondition_);
        msgEncoder.quoteCount(quoteCount);
        msgEncoder.quotesRemaining(quotesRemaining);
        msgEncoder.askQuote1().price(askQuote1_price);
        msgEncoder.askQuote1().qty(askQuote1_qty);
        msgEncoder.askQuote2().price(askQuote2_price);
        msgEncoder.askQuote2().qty(askQuote2_qty);
        msgEncoder.askQuote3().price(askQuote3_price);
        msgEncoder.askQuote3().qty(askQuote3_qty);
        msgEncoder.askQuote4().price(askQuote4_price);
        msgEncoder.askQuote4().qty(askQuote4_qty);
        msgEncoder.askQuote5().price(askQuote5_price);
        msgEncoder.askQuote5().qty(askQuote5_qty);
        msgEncoder.askQuote6().price(askQuote6_price);
        msgEncoder.askQuote6().qty(askQuote6_qty);
        msgEncoder.askQuote7().price(askQuote7_price);
        msgEncoder.askQuote7().qty(askQuote7_qty);
        msgEncoder.askQuote8().price(askQuote8_price);
        msgEncoder.askQuote8().qty(askQuote8_qty);
        msgEncoder.askQuote9().price(askQuote9_price);
        msgEncoder.askQuote9().qty(askQuote9_qty);
        msgEncoder.askQuote10().price(askQuote10_price);
        msgEncoder.askQuote10().qty(askQuote10_qty);
    }
}
