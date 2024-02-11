package com.bht.md.messages.encodehelpers;

import com.bht.md.messages.GroupedSidedUpdateEncoder;
import com.bht.md.messages.MarketState;
import com.bht.md.messages.MessageHeaderEncoder;
import com.bht.md.messages.QuoteCondition;
import org.agrona.MutableDirectBuffer;

public class GroupedSidedUpdateEncodeHelper {

    public void encodeGroupedSidedUpdate(
            GroupedSidedUpdateEncoder msgEncoder,
            MutableDirectBuffer buffer,
            MessageHeaderEncoder headerEnc,
            long time,
            short symb,
            MarketState marketstate_,
            QuoteCondition quotecondition_,
            short quotesRemaining,
            QuoteGroupRepeatingGroupHandler quoteGroup) {
        msgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        msgEncoder.time(time);
        msgEncoder.symb(symb);
        msgEncoder.marketState(marketstate_);
        msgEncoder.quoteCondition(quotecondition_);
        msgEncoder.quotesRemaining(quotesRemaining);
        // TODO: Implement decoding for unknown type, probably repeating group null, name=quoteGroup
        quoteGroup.handleQuoteGroup(msgEncoder);
    }
}
