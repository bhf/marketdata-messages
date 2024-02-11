package com.bht.md.aeron;

import com.bht.md.perfmeta.PerformanceMetaManager;
import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.driver.MediaDriver;
import io.aeron.logbuffer.BufferClaim;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.IdleStrategy;

/**
 * Set up an Aeron publication and publishing over exclusive and non-exclusive mode. Must call
 * init().
 *
 * <p>For exclusive publication you must call getExclusivePublishBuffer to get the BufferClaim and
 * once you fill it, make sure you call doExclusivePublish()
 */
public class AeronPublisher {

    private final int streamId;
    private final String channel;
    private final boolean embeddedMediaDriver;
    final BufferClaim bufferClaim = new BufferClaim();
    private final PerformanceMetaManager perfMeta;
    private Publication publication;
    private MediaDriver driver;
    private final IdleStrategy idleStrategy;
    private final boolean exclusivePub;
    private long backPressureCount = 0;
    private long totalMessageCount = 0;
    private long maxPosExceededCount = 0;
    private long publicationClosedCount = 0;
    private long adminActionCount = 0;
    private long publisherNotConnectedCount = 0;

    public boolean isExclusivePub() {
        return exclusivePub;
    }

    public AeronPublisher(
            int streamId,
            String channel,
            boolean useEmbeddedMediaDriver,
            IdleStrategy idleStrat,
            boolean exclusive,
            PerformanceMetaManager mgr) {
        this.streamId = streamId;
        this.channel = channel;
        this.embeddedMediaDriver = useEmbeddedMediaDriver;
        this.idleStrategy = idleStrat;
        this.exclusivePub = exclusive;
        this.perfMeta = mgr;
    }

    public void init() {
        driver = embeddedMediaDriver ? MediaDriver.launchEmbedded() : null;

        final Aeron.Context ctx = new Aeron.Context();
        if (embeddedMediaDriver) {
            ctx.aeronDirectoryName(driver.aeronDirectoryName());
        }

        Aeron aeron = Aeron.connect(ctx);

        if (!exclusivePub) {
            publication = aeron.addPublication(channel, streamId);
        } else {
            publication = aeron.addExclusivePublication(channel, streamId);
        }
    }

    public void persistPublisherSessionDetails(String publisherName) {
        int session = getSessionId();
        int stream = getStreamId();
        String chan = getChannel();
        perfMeta.handlePublisherDetails(session, stream, chan, publisherName);
    }

    public int getSessionId() {
        return publication.sessionId();
    }

    public int getStreamId() {
        return publication.streamId();
    }

    public String getChannel() {
        return publication.channel();
    }

    public boolean shutdown() {
        if (null != publication) {
            publication.close();
            return publication.isClosed();
        }

        return true;
    }

    public BufferClaim getPublishBuffer(int msgLen) {

        if (exclusivePub) {
            return getExclusivePublishBuffer(msgLen);
        } else {
            throw new UnsupportedOperationException(
                    "Can't get the publish buffer under non-exclusive publication mode");
        }
    }

    public void doNonExclusivePublish(DirectBuffer buffer, int msgLen) {
        idleStrategy.reset();

        long result;

        while ((result = publication.offer(buffer, 0, msgLen)) <= 0) {
            handleNonPublishEvent(result);
            idleStrategy.idle();
        }

        ++totalMessageCount;
    }

    private void handleMaxPosExceededOnPublish() {
        ++maxPosExceededCount;
    }

    private void handlePublicationClosedOnPublish() {
        ++publicationClosedCount;
    }

    private void handleAdminActionOnPublish() {
        ++adminActionCount;
    }

    private void handlePublisherNotConnectedOnPublish() {
        ++publisherNotConnectedCount;
    }

    private void handleBackPressureOnPublish() {
        ++backPressureCount;
    }

    private BufferClaim getExclusivePublishBuffer(int msgLen) {
        idleStrategy.reset();

        long result = 0;
        while ((result = publication.tryClaim(msgLen, bufferClaim)) <= 0) {
            handleNonPublishEvent(result);
            idleStrategy.idle();
        }

        return bufferClaim;
    }

    private void handleNonPublishEvent(long result) {
        if (result == Publication.BACK_PRESSURED) {
            handleBackPressureOnPublish();
            persistCounters();
        } else if (result == Publication.NOT_CONNECTED) {
            handlePublisherNotConnectedOnPublish();
        } else if (result == Publication.ADMIN_ACTION) {
            handleAdminActionOnPublish();
        } else if (result == Publication.CLOSED) {
            handlePublicationClosedOnPublish();
        } else if (result == Publication.MAX_POSITION_EXCEEDED) {
            handleMaxPosExceededOnPublish();
            persistCounters();
        }
    }

    private void persistCounters() {
        long now = System.currentTimeMillis();
        perfMeta.handleAeronCounters(
                now,
                backPressureCount,
                totalMessageCount,
                maxPosExceededCount,
                publicationClosedCount,
                adminActionCount,
                publisherNotConnectedCount);
    }

    public void doExclusivePublish() {
        bufferClaim.commit();
        ++totalMessageCount;
    }

    public long getBackPressureCount() {
        return backPressureCount;
    }

    public long getTotalMessageCount() {
        return totalMessageCount;
    }
}
