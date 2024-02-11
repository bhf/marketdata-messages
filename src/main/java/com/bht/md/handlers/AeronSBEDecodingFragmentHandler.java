package com.bht.md.handlers;

import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;

/** Decode the SBE messages directly on the Aeron subscriber thread */
public class AeronSBEDecodingFragmentHandler implements FragmentHandler {
    final SBEDecodingHandler sbeDecoder;

    public AeronSBEDecodingFragmentHandler(SBEDecodingHandler sbeDecoder) {
        this.sbeDecoder = sbeDecoder;
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        int session = header.sessionId();
        int stream = header.streamId();
        sbeDecoder.decode(buffer, offset, length, stream, session);
    }
}
