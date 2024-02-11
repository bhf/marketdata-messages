package com.bht.md.messages.encodehelpers;

import com.bht.md.messages.MarketState;
import com.bht.md.messages.MessageHeaderEncoder;
import com.bht.md.messages.MultiSymbolMassAsksUpdateEncoder;
import com.bht.md.messages.QuoteCondition;
import org.agrona.MutableDirectBuffer;

public class MultiSymbolMassAsksUpdateEncodeHelper {

    public void encodeMultiSymbolMassAsksUpdate(
            MultiSymbolMassAsksUpdateEncoder msgEncoder,
            MutableDirectBuffer buffer,
            MessageHeaderEncoder headerEnc,
            long time,
            MarketState marketstate_,
            QuoteCondition quotecondition_,
            short quoteCount,
            short quotesRemaining,
            short askQuote1_symb,
            long askQuote1_price,
            long askQuote1_qty,
            short askQuote2_symb,
            long askQuote2_price,
            long askQuote2_qty,
            short askQuote3_symb,
            long askQuote3_price,
            long askQuote3_qty,
            short askQuote4_symb,
            long askQuote4_price,
            long askQuote4_qty,
            short askQuote5_symb,
            long askQuote5_price,
            long askQuote5_qty,
            short askQuote6_symb,
            long askQuote6_price,
            long askQuote6_qty,
            short askQuote7_symb,
            long askQuote7_price,
            long askQuote7_qty,
            short askQuote8_symb,
            long askQuote8_price,
            long askQuote8_qty,
            short askQuote9_symb,
            long askQuote9_price,
            long askQuote9_qty,
            short askQuote10_symb,
            long askQuote10_price,
            long askQuote10_qty) {
        msgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        msgEncoder.time(time);
        msgEncoder.marketState(marketstate_);
        msgEncoder.quoteCondition(quotecondition_);
        msgEncoder.quoteCount(quoteCount);
        msgEncoder.quotesRemaining(quotesRemaining);
        msgEncoder.askQuote1().symb(askQuote1_symb);
        msgEncoder.askQuote1().price(askQuote1_price);
        msgEncoder.askQuote1().qty(askQuote1_qty);
        msgEncoder.askQuote2().symb(askQuote2_symb);
        msgEncoder.askQuote2().price(askQuote2_price);
        msgEncoder.askQuote2().qty(askQuote2_qty);
        msgEncoder.askQuote3().symb(askQuote3_symb);
        msgEncoder.askQuote3().price(askQuote3_price);
        msgEncoder.askQuote3().qty(askQuote3_qty);
        msgEncoder.askQuote4().symb(askQuote4_symb);
        msgEncoder.askQuote4().price(askQuote4_price);
        msgEncoder.askQuote4().qty(askQuote4_qty);
        msgEncoder.askQuote5().symb(askQuote5_symb);
        msgEncoder.askQuote5().price(askQuote5_price);
        msgEncoder.askQuote5().qty(askQuote5_qty);
        msgEncoder.askQuote6().symb(askQuote6_symb);
        msgEncoder.askQuote6().price(askQuote6_price);
        msgEncoder.askQuote6().qty(askQuote6_qty);
        msgEncoder.askQuote7().symb(askQuote7_symb);
        msgEncoder.askQuote7().price(askQuote7_price);
        msgEncoder.askQuote7().qty(askQuote7_qty);
        msgEncoder.askQuote8().symb(askQuote8_symb);
        msgEncoder.askQuote8().price(askQuote8_price);
        msgEncoder.askQuote8().qty(askQuote8_qty);
        msgEncoder.askQuote9().symb(askQuote9_symb);
        msgEncoder.askQuote9().price(askQuote9_price);
        msgEncoder.askQuote9().qty(askQuote9_qty);
        msgEncoder.askQuote10().symb(askQuote10_symb);
        msgEncoder.askQuote10().price(askQuote10_price);
        msgEncoder.askQuote10().qty(askQuote10_qty);
    }
}
