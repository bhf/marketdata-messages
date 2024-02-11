package com.bht.md.messages.encodehelpers;

import com.bht.md.messages.*;
import org.agrona.MutableDirectBuffer;

public class MultiSymbolMassSidedUpdateEncodeHelper {

    public void encodeMultiSymbolMassSidedUpdate(
            MultiSymbolMassSidedUpdateEncoder msgEncoder,
            MutableDirectBuffer buffer,
            MessageHeaderEncoder headerEnc,
            long time,
            MarketState marketstate_,
            QuoteCondition quotecondition_,
            short quoteCount,
            short quotesRemaining,
            short sidedQuote1_symb,
            long sidedQuote1_price,
            long sidedQuote1_qty,
            QuoteSide sidedQuote1_side,
            short sidedQuote2_symb,
            long sidedQuote2_price,
            long sidedQuote2_qty,
            QuoteSide sidedQuote2_side,
            short sidedQuote3_symb,
            long sidedQuote3_price,
            long sidedQuote3_qty,
            QuoteSide sidedQuote3_side,
            short sidedQuote4_symb,
            long sidedQuote4_price,
            long sidedQuote4_qty,
            QuoteSide sidedQuote4_side,
            short sidedQuote5_symb,
            long sidedQuote5_price,
            long sidedQuote5_qty,
            QuoteSide sidedQuote5_side,
            short sidedQuote6_symb,
            long sidedQuote6_price,
            long sidedQuote6_qty,
            QuoteSide sidedQuote6_side,
            short sidedQuote7_symb,
            long sidedQuote7_price,
            long sidedQuote7_qty,
            QuoteSide sidedQuote7_side,
            short sidedQuote8_symb,
            long sidedQuote8_price,
            long sidedQuote8_qty,
            QuoteSide sidedQuote8_side,
            short sidedQuote9_symb,
            long sidedQuote9_price,
            long sidedQuote9_qty,
            QuoteSide sidedQuote9_side,
            short sidedQuote10_symb,
            long sidedQuote10_price,
            long sidedQuote10_qty,
            QuoteSide sidedQuote10_side) {
        msgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        msgEncoder.time(time);
        msgEncoder.marketState(marketstate_);
        msgEncoder.quoteCondition(quotecondition_);
        msgEncoder.quoteCount(quoteCount);
        msgEncoder.quotesRemaining(quotesRemaining);
        msgEncoder.sidedQuote1().symb(sidedQuote1_symb);
        msgEncoder.sidedQuote1().price(sidedQuote1_price);
        msgEncoder.sidedQuote1().qty(sidedQuote1_qty);
        msgEncoder.sidedQuote1().side(sidedQuote1_side);
        msgEncoder.sidedQuote2().symb(sidedQuote2_symb);
        msgEncoder.sidedQuote2().price(sidedQuote2_price);
        msgEncoder.sidedQuote2().qty(sidedQuote2_qty);
        msgEncoder.sidedQuote2().side(sidedQuote2_side);
        msgEncoder.sidedQuote3().symb(sidedQuote3_symb);
        msgEncoder.sidedQuote3().price(sidedQuote3_price);
        msgEncoder.sidedQuote3().qty(sidedQuote3_qty);
        msgEncoder.sidedQuote3().side(sidedQuote3_side);
        msgEncoder.sidedQuote4().symb(sidedQuote4_symb);
        msgEncoder.sidedQuote4().price(sidedQuote4_price);
        msgEncoder.sidedQuote4().qty(sidedQuote4_qty);
        msgEncoder.sidedQuote4().side(sidedQuote4_side);
        msgEncoder.sidedQuote5().symb(sidedQuote5_symb);
        msgEncoder.sidedQuote5().price(sidedQuote5_price);
        msgEncoder.sidedQuote5().qty(sidedQuote5_qty);
        msgEncoder.sidedQuote5().side(sidedQuote5_side);
        msgEncoder.sidedQuote6().symb(sidedQuote6_symb);
        msgEncoder.sidedQuote6().price(sidedQuote6_price);
        msgEncoder.sidedQuote6().qty(sidedQuote6_qty);
        msgEncoder.sidedQuote6().side(sidedQuote6_side);
        msgEncoder.sidedQuote7().symb(sidedQuote7_symb);
        msgEncoder.sidedQuote7().price(sidedQuote7_price);
        msgEncoder.sidedQuote7().qty(sidedQuote7_qty);
        msgEncoder.sidedQuote7().side(sidedQuote7_side);
        msgEncoder.sidedQuote8().symb(sidedQuote8_symb);
        msgEncoder.sidedQuote8().price(sidedQuote8_price);
        msgEncoder.sidedQuote8().qty(sidedQuote8_qty);
        msgEncoder.sidedQuote8().side(sidedQuote8_side);
        msgEncoder.sidedQuote9().symb(sidedQuote9_symb);
        msgEncoder.sidedQuote9().price(sidedQuote9_price);
        msgEncoder.sidedQuote9().qty(sidedQuote9_qty);
        msgEncoder.sidedQuote9().side(sidedQuote9_side);
        msgEncoder.sidedQuote10().symb(sidedQuote10_symb);
        msgEncoder.sidedQuote10().price(sidedQuote10_price);
        msgEncoder.sidedQuote10().qty(sidedQuote10_qty);
        msgEncoder.sidedQuote10().side(sidedQuote10_side);
    }
}
