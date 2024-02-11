package com.bht.md.ringbuffers;

import org.agrona.concurrent.AtomicBuffer;

public interface StreamSessionMessageHandler {

    public void onMessage(int messageTypeId, AtomicBuffer buffer, int i, int i1, int streamId, int sessionId);
}
