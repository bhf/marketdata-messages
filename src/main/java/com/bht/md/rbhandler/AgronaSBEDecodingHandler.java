package com.bht.md.rbhandler;

import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.handlers.SBEDecodingHandler;
import org.agrona.concurrent.MessageHandler;
import org.agrona.concurrent.ringbuffer.RingBuffer;

/** Process the Agrona ringbuffer, passing messages to the message handler to be decoded */
public class AgronaSBEDecodingHandler {

    final SBEDecodingHandler sbeDecodingHandler;
    final RingBuffer rb;
    final MessageHandler msgHandler;

    public AgronaSBEDecodingHandler(AbstractSBEDecodingHandler sbeDecodingHandler, RingBuffer rb) {
        this.sbeDecodingHandler = sbeDecodingHandler;
        this.rb = rb;
        this.msgHandler = new AgronaMessageHandler(sbeDecodingHandler);
    }

    public void processRingbuffer() {
        rb.read(msgHandler);
    }
}
