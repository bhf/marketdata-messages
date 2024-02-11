package com.bht.md.rbpublishers;

import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import org.agrona.DirectBuffer;

/** Write the raw SBE message onto a Chronicle queue from the Aeron subscriber thread. */
public class SBEToChronicleQueueFragmentHandler implements FragmentHandler {

    final ChronicleQueue queue;
    final ExcerptAppender appender;
    final BufferMarshallable marshall;

    int msgsWritten = 0;

    public SBEToChronicleQueueFragmentHandler(ChronicleQueue q) {
        this.queue = q;
        appender = q.createAppender();
        marshall = new BufferMarshallable();
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        int stream = header.streamId();
        int session = header.sessionId();
        marshall.setRefs(offset, length, buffer, stream, session);
        appender.writeBytes(marshall);
        msgsWritten++;
    }
}
