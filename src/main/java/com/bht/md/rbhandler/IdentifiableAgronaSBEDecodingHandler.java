package com.bht.md.rbhandler;

import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.handlers.SBEDecodingHandler;
import com.bht.md.ringbuffers.IdentifyingManyToOneRingBuffer;

/** Process the Agrona ringbuffer,
 * passing messages to the message handler to be decoded. This is
 * for the variant where we are getting info on the stream and session*/
public class IdentifiableAgronaSBEDecodingHandler {

    final SBEDecodingHandler sbeDecodingHandler;
    final IdentifyingManyToOneRingBuffer rb;
    final IdentifiableAgronaMessageHandler msgHandler;

    public IdentifiableAgronaSBEDecodingHandler(
            AbstractSBEDecodingHandler sbeDecodingHandler, IdentifyingManyToOneRingBuffer rb) {
        this.sbeDecodingHandler = sbeDecodingHandler;
        this.rb = rb;
        this.msgHandler = new IdentifiableAgronaMessageHandler(sbeDecodingHandler);
    }

    public void processRingbuffer() {
        rb.read(msgHandler);
    }
}
