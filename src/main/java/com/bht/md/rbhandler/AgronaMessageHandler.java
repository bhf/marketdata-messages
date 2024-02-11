package com.bht.md.rbhandler;

import com.bht.md.handlers.AbstractSBEDecodingHandler;
import com.bht.md.handlers.SBEDecodingHandler;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.MessageHandler;

/** Handler used by the Agrona ring buffer to process messages
 * Packs the stream and session id into the agrona message type*/
public class AgronaMessageHandler implements MessageHandler {

    private final SBEDecodingHandler sbeDecodingHandler;

    public AgronaMessageHandler(AbstractSBEDecodingHandler sbeDecodingHandler) {
        this.sbeDecodingHandler = sbeDecodingHandler;
    }

    @Override
    public void onMessage(int i, MutableDirectBuffer mutableDirectBuffer, int i1, int i2) {
        unpack2(i);
        final int lastStream = unpacked[0];
        final int lastSession = unpacked[1];
        sbeDecodingHandler.decode(mutableDirectBuffer, i1, i2, lastStream, lastSession);
    }

    int[] unpacked = new int[2];

    void unpack2(int packed) {
        int val1 = ((packed >> 16) & 0xFFFF);
        if ((val1 & 0x8000) != 0) val1 |= 0xFFFF0000;

        int val2 = (packed & 0xFFFF);
        if ((val2 & 0x8000) != 0) val2 |= 0xFFFF0000;

        unpacked[0] = val1;
        unpacked[1] = val2;
    }
}
