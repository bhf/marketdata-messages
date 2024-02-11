package com.bht.md.aeron;

import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.concurrent.IdleStrategy;

/**
 * Setup an Aeron subscription. Must call init(). Call doWork(..) in your duty cycle, passing in
 * your FragmentHandler of choice.
 */
public class AeronSubscriber {

    private final int streamId;
    private final String channel;
    private final boolean embeddedMediaDriver;

    private MediaDriver driver;
    private final IdleStrategy idleStrategy;

    private Subscription subscription;

    public AeronSubscriber(int streamId_, String channelId_, boolean embedded_, IdleStrategy idleStrat) {
        this.streamId = streamId_;
        this.channel = channelId_;
        this.embeddedMediaDriver = embedded_;
        this.idleStrategy = idleStrat;
    }

    public void init() {
        driver = embeddedMediaDriver ? MediaDriver.launchEmbedded() : null;

        final Aeron.Context ctx = new Aeron.Context();
        if (embeddedMediaDriver) {
            ctx.aeronDirectoryName(driver.aeronDirectoryName());
        }

        Aeron aeron = Aeron.connect(ctx);
        subscription = aeron.addSubscription(channel, streamId);
    }

    public boolean shutdown() {
        if (null != subscription) {
            subscription.close();
            return subscription.isClosed();
        }
        return true;
    }

    int polls = 0;

    public void doWork(FragmentHandler handler) {
        idleStrategy.reset();
        subscription.poll(handler, Integer.MAX_VALUE);
        idleStrategy.idle();
        polls++;
    }
}
