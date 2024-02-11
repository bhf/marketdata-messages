package com.bht.md.messages.encodehelpers;

import com.bht.md.messages.MarketState;
import com.bht.md.messages.MarketStateEncoder;
import com.bht.md.messages.MessageHeaderEncoder;
import org.agrona.MutableDirectBuffer;

public class MarketStateEncodeHelper {

    public void encodeMarketState(
            MarketStateEncoder msgEncoder,
            MutableDirectBuffer buffer,
            MessageHeaderEncoder headerEnc,
            long time,
            short symb,
            int marketID,
            MarketState marketstate_) {
        msgEncoder.wrapAndApplyHeader(buffer, 0, headerEnc);
        msgEncoder.time(time);
        msgEncoder.symb(symb);
        msgEncoder.marketID(marketID);
        msgEncoder.marketState(marketstate_);
    }
}
