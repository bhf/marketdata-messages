package com.bht.md.rbpublishers;

import com.lmax.disruptor.RingBuffer;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;

/** Write the raw SBE message onto a Disruptor ring buffer from the Aeron subscriber thread. */
public class SBEToDisruptorFragmentHandler implements FragmentHandler {

    final RingBuffer<BufferEvent> rb;

    public SBEToDisruptorFragmentHandler(RingBuffer<BufferEvent> rb) {
        this.rb = rb;
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        long next = rb.next();
        BufferEvent bufferEvent = rb.get(next);
        bufferEvent.clear();
        bufferEvent.offset = offset;
        bufferEvent.length = length;
        buffer.getBytes(offset, bufferEvent.buffer, 0, length);

        rb.publish(next);
    }
}
