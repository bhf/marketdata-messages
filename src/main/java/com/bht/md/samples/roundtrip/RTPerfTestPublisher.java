package com.bht.md.samples.roundtrip;

import com.bht.md.aeron.AeronPublisher;
import com.bht.md.aeron.AeronSubscriber;
import com.bht.md.handlers.AeronSBEDecodingFragmentHandler;
import com.bht.md.handlers.MessageBasedRTRecorder;
import com.bht.md.handlers.SBEDecodingHandler;
import com.bht.md.perfmeta.ChronicleBasedPerfManager;
import com.bht.md.perfmeta.NoOpPerfMetaManager;
import com.bht.md.perfmeta.PerformanceMetaManager;
import com.bht.md.util.ThreadFactoryUtil;
import io.aeron.logbuffer.FragmentHandler;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Conduct a roundtrip performance test. User can select the channel type, idle strategies and
 * whether the publish is exclusive (tryClaim) or not.
 *
 * <p>Publishes nanosecond timestamped messages from a single thread, receives the same type of
 * message echoed back with the original sending timestamp which is used to calculate a time delta
 * between sending and recieving.
 */
public class RTPerfTestPublisher {
    public static final String RTMSGS = "-RTMSGS";
    public static final String CASEDETS = "-CASEDETS";
    public static final String AERONCTRS = "-AERONCTRS";
    public static final String TIMEOFFSETS = "-TIMEOFFSETS";
    public static final String PUBDETS = "-ECHOPUBDETS";

    final Logger logger = LoggerFactory.getLogger(RTPerfTestPublisher.class);

    public static void main(String[] args) {

        Properties properties = loadTestCaseProperties(args[0]);
        String prefix = properties.getProperty("prefix");
        String logName = prefix + "-" + "PublisherRT";
        System.setProperty("log.name", logName);

        int streamId = Integer.parseInt(properties.getProperty("streamId"));
        int echoStreamId = Integer.parseInt(properties.getProperty("echoStreamId"));
        String channel = properties.getProperty("channel");
        String echoChanId = properties.getProperty("echoChanId");
        String basePath = properties.getProperty("basePath");
        boolean embedded = Boolean.parseBoolean(properties.getProperty("embedded"));
        boolean exclusive = Boolean.parseBoolean(properties.getProperty("exclusive"));
        int subscriberCpuId = Integer.parseInt(properties.getProperty("subscriberCpuId"));

        int warmupCount = Integer.parseInt(properties.getProperty("warmupCount"));

        PerformanceMetaManager perfManager = getPerformanceMetaTracker(prefix, basePath);

        long startTime = System.currentTimeMillis();
        RTPerfTestPublisher perfTest = new RTPerfTestPublisher();
        perfTest.setupPubSub(
                echoStreamId,
                echoChanId,
                embedded,
                streamId,
                channel,
                exclusive,
                subscriberCpuId,
                startTime,
                perfManager,
                warmupCount);
    }

    private static PerformanceMetaManager getPerformanceMetaTracker(String prefix, String basePath) {
        ChronicleQueue roundtrip = SingleChronicleQueueBuilder.single(basePath + "/" + prefix + RTMSGS)
                .build();
        ChronicleQueue testcaseDets = SingleChronicleQueueBuilder.single(basePath + "/" + prefix + CASEDETS)
                .build();
        ChronicleQueue timeOffets = SingleChronicleQueueBuilder.single(basePath + "/" + prefix + TIMEOFFSETS)
                .build();
        ChronicleQueue aeronCtr = SingleChronicleQueueBuilder.single(basePath + "/" + prefix + AERONCTRS)
                .build();
        ChronicleQueue publisherDets = SingleChronicleQueueBuilder.single(basePath + "/" + prefix + PUBDETS)
                .build();
        return new ChronicleBasedPerfManager(roundtrip, testcaseDets, timeOffets, aeronCtr, publisherDets);
    }

    private void setupPubSub(
            int subscriptionStream,
            String channel,
            boolean embedded,
            int publicationStream,
            String publicationChannel,
            boolean exclusivePub,
            int publisherCpuId,
            long startTime,
            PerformanceMetaManager perfManager,
            int warmupCount) {
        IdleStrategy idleStrat = getIdleStrategy();

        AeronSubscriber echoSubscriber = new AeronSubscriber(subscriptionStream, channel, embedded, getIdleStrategy());
        AeronPublisher pub = new AeronPublisher(
                publicationStream, publicationChannel, embedded, idleStrat, exclusivePub, new NoOpPerfMetaManager());

        startPublisher(pub, getIdleStrategy(), echoSubscriber, perfManager, publisherCpuId, startTime, warmupCount);
    }

    private void startPublisher(
            AeronPublisher pub,
            IdleStrategy idleStrategy,
            AeronSubscriber echoSubscriber,
            PerformanceMetaManager perfManager,
            int publisherCpuId,
            long startTime,
            int warmupCount) {

        echoSubscriber.init();
        pub.init();

        MessageBasedRTRecorder recorder = new MessageBasedRTRecorder(perfManager, startTime, warmupCount);
        FragmentHandler echoListeningFragmentHandler = getEchoListener(recorder);

        logger.info("Initialising RT publisher");

        ExecutorService es = publisherCpuId > 0
                ? Executors.newSingleThreadExecutor(
                        ThreadFactoryUtil.getThreadFactory("EchoReceivingPublisher", publisherCpuId))
                : Executors.newSingleThreadExecutor();

        es.execute(new EchoReceivingPublisher(echoListeningFragmentHandler, pub, echoSubscriber, idleStrategy));
    }

    private FragmentHandler getEchoListener(SBEDecodingHandler histograms) {
        return new AeronSBEDecodingFragmentHandler(histograms);
    }

    private IdleStrategy getIdleStrategy() {
        return new BusySpinIdleStrategy();
    }

    public static Properties loadTestCaseProperties(String propertiesFile) {

        String appConfigPath = propertiesFile;
        Properties appProps = new Properties();

        try (FileInputStream fis = new FileInputStream(appConfigPath)) {
            appProps.load(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return appProps;
    }
}
