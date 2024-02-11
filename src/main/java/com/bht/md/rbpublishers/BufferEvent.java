package com.bht.md.rbpublishers;

import org.agrona.MutableDirectBuffer;

/**
 * Event made up of multiple MutableDirectBuffers. The Aeron term buffer is being copied into these
 * buffers via UNSAFE through AbstractMutableDirectBuffer before being published to the Disruptor
 * ring buffer.
 */
public class BufferEvent {
    public final MutableDirectBuffer buffer;
    public int offset;
    public int length;
    public int stream;
    public int session;

    public BufferEvent(MutableDirectBuffer buffer) {
        this.buffer = buffer;
    }

    public void clear() {
        offset = 0;
        length = 0;
        stream = 0;
        session = 0;
    }
}
