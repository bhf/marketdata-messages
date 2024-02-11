package com.bht.md.samples.clean;

import com.bht.md.perfmeta.ChronicleBasedPerfManager;
import com.bht.md.perfmeta.PerformanceMetaManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubRTRunner {

    public static final String CASEDETS = "-CASEDETS";
    public static final String AERONCTRS = "-AERONCTRS";
    public static final String PUBDETS = "-ECHOPUBDETS";

    final Logger logger = LoggerFactory.getLogger(SubRTRunner.class);

    public static void main(String[] args) {
        SubRTRunner runner = new SubRTRunner();
        Properties properties = runner.loadTestCaseProperties(args[0]);
        String prefix = properties.getProperty("prefix");
        ParadigmType p = ParadigmType.valueOf(properties.getProperty("paradigmType"));
        String logName = prefix + "-" + p + "-Subscriber";
        System.setProperty("log.name", logName);

        int streamId = Integer.parseInt(properties.getProperty("streamId"));
        int echoStreamId = Integer.parseInt(properties.getProperty("echoStreamId"));
        String channel = properties.getProperty("channel");
        String echoChanId = properties.getProperty("echoChanId");
        String basePath = properties.getProperty("basePath");
        boolean embedded = Boolean.parseBoolean(properties.getProperty("embedded"));
        boolean exclusive = Boolean.parseBoolean(properties.getProperty("exclusive"));

        int subscriberCpuId = Integer.parseInt(properties.getProperty("subscriberCpuId"));

        PerformanceMetaManager perfTrack = getPerformanceMetaTracker(prefix, basePath);
        AbstractSubscriberRTTesting subTest = getSubscriberRTType(p);

        // disruptor requires the event handler to be wired in, which requires
        // an echo back handler to be setup in advance of the sub to Aeron
        if (p == ParadigmType.DISRUPTOR) {
            subTest.setupSubToAeron(
                    streamId, channel, echoStreamId, echoChanId, embedded, exclusive, perfTrack, subscriberCpuId);

            subTest.setupParadigmSpecific(streamId, channel, echoStreamId, echoChanId, embedded, exclusive, perfTrack);
        } else {
            subTest.setupParadigmSpecific(streamId, channel, echoStreamId, echoChanId, embedded, exclusive, perfTrack);

            subTest.setupSubToAeron(
                    streamId, channel, echoStreamId, echoChanId, embedded, exclusive, perfTrack, subscriberCpuId);
        }

        subTest.startProcessingSub();
    }

    private Properties loadTestCaseProperties(String propertiesFile) {

        String appConfigPath = propertiesFile;
        Properties appProps = new Properties();

        try (FileInputStream fis = new FileInputStream(appConfigPath)) {
            appProps.load(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Loaded properties from file:" + propertiesFile);
        logger.info("Properties:" + appProps);

        return appProps;
    }

    private static PerformanceMetaManager getPerformanceMetaTracker(String prefix, String basePath) {
        ChronicleQueue roundtrip = null;
        ChronicleQueue testcaseDets = SingleChronicleQueueBuilder.single(basePath + "/" + prefix + CASEDETS)
                .build();
        ChronicleQueue timeOffets = null;
        ChronicleQueue aeronCtr = SingleChronicleQueueBuilder.single(basePath + "/" + prefix + AERONCTRS)
                .build();
        ChronicleQueue publisherDets = SingleChronicleQueueBuilder.single(basePath + "/" + prefix + PUBDETS)
                .build();
        return new ChronicleBasedPerfManager(roundtrip, testcaseDets, timeOffets, aeronCtr, publisherDets);
    }

    private static AbstractSubscriberRTTesting getSubscriberRTType(ParadigmType t) {
        switch (t) {
            case AERON: {
                // SBE Decoding is done on the Aeron subscriber thread i.e. in the FragmentHandler
                return new AeronSubRT();
            }
            case AGRONA: {
                // SBE decoding is done on the consuming thread of the Agrona RB
                return new AgronaSubRT();
            }
            case CHRONICLE: {
                // SBE decoding is done on a tail excerpt-ing thread on a Chronicle queue
                return new ChronicleSubRT();
            }
            case DISRUPTOR: {
                // SBE decoding is done on the disruptor event handling thread
                return new DisruptorSubRT();
            }
            default:
                return null;
        }
    }

    public enum ParadigmType {
        AERON,
        AGRONA,
        CHRONICLE,
        DISRUPTOR
    }
}
