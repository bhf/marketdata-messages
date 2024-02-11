package com.bht.md.rbpublishers;

import com.bht.md.ringbuffers.IdentifyingManyToOneRingBuffer;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;

public class SBEToAgronaIdentifiableRingbufferFragmentHandler implements FragmentHandler {
    private final IdentifyingManyToOneRingBuffer rb;

    public SBEToAgronaIdentifiableRingbufferFragmentHandler(IdentifyingManyToOneRingBuffer rb) {
        this.rb = rb;
    }

    @Override
    public void onFragment(DirectBuffer directBuffer, int i, int i1, Header header) {
        int sessionId = header.sessionId();
        int streamId = header.streamId();
        rb.write(1, directBuffer, i, i1, streamId, sessionId);
    }
}
