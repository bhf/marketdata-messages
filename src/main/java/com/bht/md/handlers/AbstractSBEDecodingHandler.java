package com.bht.md.handlers;

import com.bht.md.messages.*;
import org.agrona.DirectBuffer;

/**
 * Autogenerated from SBE messages xml
 */
public abstract class AbstractSBEDecodingHandler implements SBEDecodingHandler {

    MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();

    private final SingleSidedQuoteDecoder msgDecoderSingleSidedQuote = new SingleSidedQuoteDecoder();

    private final TwoSidedQuoteDecoder msgDecoderTwoSidedQuote = new TwoSidedQuoteDecoder();

    private final MassBidsUpdateDecoder msgDecoderMassBidsUpdate = new MassBidsUpdateDecoder();

    private final MassAsksUpdateDecoder msgDecoderMassAsksUpdate = new MassAsksUpdateDecoder();

    private final MassSidedUpdateDecoder msgDecoderMassSidedUpdate = new MassSidedUpdateDecoder();

    private final GroupedSidedUpdateDecoder msgDecoderGroupedSidedUpdate = new GroupedSidedUpdateDecoder();

    private final MultiSymbolMassBidsUpdateDecoder msgDecoderMultiSymbolMassBidsUpdate =
            new MultiSymbolMassBidsUpdateDecoder();

    private final MultiSymbolMassAsksUpdateDecoder msgDecoderMultiSymbolMassAsksUpdate =
            new MultiSymbolMassAsksUpdateDecoder();

    private final MultiSymbolMassSidedUpdateDecoder msgDecoderMultiSymbolMassSidedUpdate =
            new MultiSymbolMassSidedUpdateDecoder();

    private final MarketStateDecoder msgDecoderMarketState = new MarketStateDecoder();

    @Override
    public void decode(DirectBuffer buffer, int offset, int length, int stream, int session) {

        headerDecoder.wrap(buffer, offset);
        int blockLen = headerDecoder.blockLength();
        int tid = headerDecoder.templateId();
        int schemaID = headerDecoder.schemaId();
        int version = headerDecoder.version();
        int encLen = headerDecoder.encodedLength();

        if (isValid(tid, schemaID, version)) {
            if (tid == SingleSidedQuoteEncoder.TEMPLATE_ID) {
                handleSingleSidedQuote(buffer, offset, stream, session);
            } else if (tid == TwoSidedQuoteEncoder.TEMPLATE_ID) {
                handleTwoSidedQuote(buffer, offset, stream, session);
            } else if (tid == MassBidsUpdateEncoder.TEMPLATE_ID) {
                handleMassBidsUpdate(buffer, offset, stream, session);
            } else if (tid == MassAsksUpdateEncoder.TEMPLATE_ID) {
                handleMassAsksUpdate(buffer, offset, stream, session);
            } else if (tid == MassSidedUpdateEncoder.TEMPLATE_ID) {
                handleMassSidedUpdate(buffer, offset, stream, session);
            } else if (tid == GroupedSidedUpdateEncoder.TEMPLATE_ID) {
                handleGroupedSidedUpdate(buffer, offset, stream, session);
            } else if (tid == MultiSymbolMassBidsUpdateEncoder.TEMPLATE_ID) {
                handleMultiSymbolMassBidsUpdate(buffer, offset, stream, session);
            } else if (tid == MultiSymbolMassAsksUpdateEncoder.TEMPLATE_ID) {
                handleMultiSymbolMassAsksUpdate(buffer, offset, stream, session);
            } else if (tid == MultiSymbolMassSidedUpdateEncoder.TEMPLATE_ID) {
                handleMultiSymbolMassSidedUpdate(buffer, offset, stream, session);
            } else if (tid == MarketStateEncoder.TEMPLATE_ID) {
                handleMarketState(buffer, offset, stream, session);
            }
        }
    }

    protected abstract boolean isValid(int tid, int schemaID, int version);

    private void handleSingleSidedQuote(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderSingleSidedQuote.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleSingleSidedQuote(msgDecoderSingleSidedQuote, stream, session);
    }

    public abstract void handleSingleSidedQuote(SingleSidedQuoteDecoder msgDecoder, int stream, int session);

    private void handleTwoSidedQuote(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderTwoSidedQuote.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleTwoSidedQuote(msgDecoderTwoSidedQuote, stream, session);
    }

    public abstract void handleTwoSidedQuote(TwoSidedQuoteDecoder msgDecoder, int stream, int session);

    private void handleMassBidsUpdate(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderMassBidsUpdate.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleMassBidsUpdate(msgDecoderMassBidsUpdate, stream, session);
    }

    public abstract void handleMassBidsUpdate(MassBidsUpdateDecoder msgDecoder, int stream, int session);

    private void handleMassAsksUpdate(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderMassAsksUpdate.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleMassAsksUpdate(msgDecoderMassAsksUpdate, stream, session);
    }

    public abstract void handleMassAsksUpdate(MassAsksUpdateDecoder msgDecoder, int stream, int session);

    private void handleMassSidedUpdate(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderMassSidedUpdate.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleMassSidedUpdate(msgDecoderMassSidedUpdate, stream, session);
    }

    public abstract void handleMassSidedUpdate(MassSidedUpdateDecoder msgDecoder, int stream, int session);

    private void handleGroupedSidedUpdate(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderGroupedSidedUpdate.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleGroupedSidedUpdate(msgDecoderGroupedSidedUpdate, stream, session);
    }

    public abstract void handleGroupedSidedUpdate(GroupedSidedUpdateDecoder msgDecoder, int stream, int session);

    private void handleMultiSymbolMassBidsUpdate(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderMultiSymbolMassBidsUpdate.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleMultiSymbolMassBidsUpdate(msgDecoderMultiSymbolMassBidsUpdate, stream, session);
    }

    public abstract void handleMultiSymbolMassBidsUpdate(
            MultiSymbolMassBidsUpdateDecoder msgDecoder, int stream, int session);

    private void handleMultiSymbolMassAsksUpdate(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderMultiSymbolMassAsksUpdate.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleMultiSymbolMassAsksUpdate(msgDecoderMultiSymbolMassAsksUpdate, stream, session);
    }

    public abstract void handleMultiSymbolMassAsksUpdate(
            MultiSymbolMassAsksUpdateDecoder msgDecoder, int stream, int session);

    private void handleMultiSymbolMassSidedUpdate(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderMultiSymbolMassSidedUpdate.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleMultiSymbolMassSidedUpdate(msgDecoderMultiSymbolMassSidedUpdate, stream, session);
    }

    public abstract void handleMultiSymbolMassSidedUpdate(
            MultiSymbolMassSidedUpdateDecoder msgDecoder, int stream, int session);

    private void handleMarketState(DirectBuffer buffer, int offset, int stream, int session) {
        msgDecoderMarketState.wrapAndApplyHeader(buffer, offset, headerDecoder);
        handleMarketState(msgDecoderMarketState, stream, session);
    }

    public abstract void handleMarketState(MarketStateDecoder msgDecoder, int stream, int session);
}
