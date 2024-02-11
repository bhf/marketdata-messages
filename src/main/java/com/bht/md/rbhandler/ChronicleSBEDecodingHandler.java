package com.bht.md.rbhandler;

import com.bht.md.handlers.SBEDecodingHandler;
import com.bht.md.rbpublishers.BufferMarshallable;
import java.nio.ByteBuffer;
import net.openhft.chronicle.queue.ExcerptTailer;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;

/** Process the Chronicle queue, passing messages to the message handler to be decoded */
public class ChronicleSBEDecodingHandler {

    private final ExcerptTailer tailer;
    private final BufferMarshallable marshall = new BufferMarshallable();
    private final SBEDecodingHandler sbeDecodingHandler;
    private final UnsafeBuffer buffer;
    private int msgCount;

    public ChronicleSBEDecodingHandler(ExcerptTailer tailer, SBEDecodingHandler sbeDecodingHandler) {
        this.tailer = tailer;
        this.sbeDecodingHandler = sbeDecodingHandler;

        ByteBuffer directAlignedBuffer = BufferUtil.allocateDirectAligned(512, 8);
        buffer = new UnsafeBuffer(directAlignedBuffer);
    }

    public void process() {
        boolean hasRead = tailer.readBytes(marshall);

        if (hasRead) {
            buffer.wrap(marshall.getDataBuffer());
            sbeDecodingHandler.decode(buffer, 0, marshall.getLength(), marshall.stream, marshall.session);
            msgCount++;
        }
    }
}
