package com.bht.md.rbhandler;

import com.bht.md.handlers.SBEDecodingHandler;
import com.bht.md.ringbuffers.StreamSessionMessageHandler;
import org.agrona.concurrent.AtomicBuffer;

/**
 * Agrona message handler to provide stream ID and session ID
 */
public class IdentifiableAgronaMessageHandler implements StreamSessionMessageHandler {
    private final SBEDecodingHandler sbeDecodingHandler;

    public IdentifiableAgronaMessageHandler(SBEDecodingHandler sbeDecodingHandler) {
        this.sbeDecodingHandler = sbeDecodingHandler;
    }

    @Override
    public void onMessage(int messageTypeId, AtomicBuffer buffer, int i, int i1, int streamId, int sessionId) {
        sbeDecodingHandler.decode(buffer, i, i1, streamId, sessionId);
    }
}
