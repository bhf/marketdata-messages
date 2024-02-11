package com.bht.md.handlers;

import org.agrona.DirectBuffer;

/** Abstract away the decoding of a DirectBuffer into an SBE object */
public interface SBEDecodingHandler {

    void decode(DirectBuffer buffer, int offset, int length, int stream, int session);
}
