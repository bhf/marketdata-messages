package com.bht.md.rbpublishers;

import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.ringbuffer.ManyToOneRingBuffer;

/**
 * Write the raw SBE message onto an Agrona many to one ring buffer from the Aeron subscriber
 * thread.
 */
public class SBEToAgronaRingbufferFragmentHandler implements FragmentHandler {
    private final ManyToOneRingBuffer rb;

    public SBEToAgronaRingbufferFragmentHandler(ManyToOneRingBuffer rb) {
        this.rb = rb;
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        int sessionId = header.sessionId();
        int streamId = header.streamId();
        int packed = pack(streamId, sessionId);
        rb.write(packed, buffer, offset, length);
    }

    int pack(int val1, int val2) {
        int ret_val = (((val1 & 0xFFFF) << 16) | (val2 & 0xFFFF));
        return ret_val;
    }
}
