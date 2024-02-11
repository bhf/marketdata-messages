package com.bht.md.rbhandler;

import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.handlers.SBEDecodingHandler;
import com.bht.md.rbpublishers.BufferEvent;
import com.lmax.disruptor.EventHandler;

/**
 * Handle events published on the Disruptor ringbuffer, passing messages to the message handler to
 * be decoded
 */
public class DisruptorSBEDecodingHandler implements EventHandler<BufferEvent> {

    final SBEDecodingHandler sbeDecodingHandler;

    public DisruptorSBEDecodingHandler(AbstractSBEDecodingHandler sbeDecodingHandler) {
        this.sbeDecodingHandler = sbeDecodingHandler;
    }

    @Override
    public void onEvent(BufferEvent event, long sequence, boolean endOfBatch) {
        sbeDecodingHandler.decode(event.buffer, 0, 0, event.stream, event.session);
    }
}
