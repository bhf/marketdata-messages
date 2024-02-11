package com.bht.md.handlers;

import com.bht.md.messages.*;
import com.bht.md.perfmeta.HistogramOutputMarshallable;
import io.aeron.shadow.org.HdrHistogram.AbstractHistogram;
import io.aeron.shadow.org.HdrHistogram.Histogram;
import io.aeron.shadow.org.HdrHistogram.HistogramIterationValue;
import java.io.*;
import java.util.Iterator;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import org.agrona.collections.Int2ObjectHashMap;
import org.agrona.collections.IntObjConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Track roundtrip latencies for all message types and per message type
 * Persists to a Chronicle Queue or to a text file */
public class MessageBasedRTHistogramRecorder extends AbstractSBEDecodingHandler {

    final Logger logger = LoggerFactory.getLogger(MessageBasedRTHistogramRecorder.class);
    public static final int LOGGING_FREQUENCY = 10000000;
    public static final int WARMUP_MESSAGES = 10000000 * 5;
    private static final int PERSIST_FREQUENCY = LOGGING_FREQUENCY * 5;
    private final String HISTOGRAM_LOG_DIR;
    final int MAX_BUCKETS = 1000000000;
    final Int2ObjectHashMap<Histogram> msgHistograms = new Int2ObjectHashMap<Histogram>();
    final Histogram allMsgs;
    private final String outputPrefix;
    private final long startTime;
    int msgCount = 0;
    private long endOfWarmup;

    ExcerptAppender appender;
    HistogramOutputMarshallable marshall;

    public MessageBasedRTHistogramRecorder(ChronicleQueue q, long startTime) {
        this.HISTOGRAM_LOG_DIR = null;
        this.outputPrefix = null;
        allMsgs = new Histogram(MAX_BUCKETS, 0);
        this.startTime = startTime;
        appender = q.createAppender();
        marshall = new HistogramOutputMarshallable();
    }

    public MessageBasedRTHistogramRecorder(String logOutputFolder, String outputPrefix, long startTime) {
        this.HISTOGRAM_LOG_DIR = logOutputFolder;
        this.outputPrefix = outputPrefix;
        allMsgs = new Histogram(MAX_BUCKETS, 0);
        this.startTime = startTime;
    }

    public void persist() {
        long now = System.currentTimeMillis();
        long dur = now - startTime;

        if (null == appender) {
            String baseName = outputPrefix + "-Histogram-" + msgCount + "-" + dur + "-";
            String fileName = HISTOGRAM_LOG_DIR + baseName + "-0.log";
            persistHistogram(allMsgs, fileName);
            msgHistograms.forEachInt(consumingPersister);
        } else {
            persistHistogramTID(allMsgs, 0, now);
        }
    }

    IntObjConsumer<Histogram> consumingPersister = new IntObjConsumer<Histogram>() {
        @Override
        public void accept(int i, Histogram histogram) {
            long now = System.currentTimeMillis();
            long dur = now - startTime;

            int tid = i + 1;
            String baseName = "Histogram-" + msgCount + "-" + dur + "-";
            String msgSpecificHistoFile = HISTOGRAM_LOG_DIR + baseName + "-" + tid + ".log";
            persistHistogram(histogram, msgSpecificHistoFile);
        }
    };

    IntObjConsumer<Histogram> consumingChroniclePersister = new IntObjConsumer<Histogram>() {
        @Override
        public void accept(int i, Histogram histogram) {
            long now = System.currentTimeMillis();
            int tid = i + 1;
            persistHistogramTID(histogram, tid, now);
        }
    };

    private void persistHistogram(Histogram histogram, String msgSpecificHistoFile) {
        try (PrintStream msgSpecificOutStream = new PrintStream(msgSpecificHistoFile); ) {
            histogram.outputPercentileDistribution(msgSpecificOutStream, 1.0);
        } catch (FileNotFoundException e) {
            logger.error("Couldn't create file for histogram output: " + msgSpecificHistoFile);
            throw new RuntimeException(e);
        }
    }

    /**
     * Persist the histogram data to a chronicle for now
     * as printstream is creating too much garbage
     * @param histogram
     * @param tid
     */
    private void persistHistogramTID(Histogram histogram, int tid, long time) {
        AbstractHistogram.Percentiles percentiles = histogram.percentiles(5);
        Iterator<HistogramIterationValue> iterator = percentiles.iterator();
        long msgCount = histogram.getTotalCount();
        while (iterator.hasNext()) {
            HistogramIterationValue iterationValue = iterator.next();
            if (iterationValue.getPercentileLevelIteratedTo() != 100.0) {
                double val1 = iterationValue.getValueIteratedTo();
                double val2 = iterationValue.getPercentileLevelIteratedTo() / 100.0;
                double val3 = iterationValue.getTotalCountToThisValue();
                double val4 = 1.0 / (1.0 - iterationValue.getPercentileLevelIteratedTo() / 100.0);
                marshall.updateValues(val1, val2, val3, val4, tid, msgCount, time);
                appender.writeBytes(marshall);
            } else {
                double val1 = iterationValue.getValueIteratedTo();
                double val2 = iterationValue.getPercentileLevelIteratedTo() / 100.0;
                double val3 = iterationValue.getTotalCountToThisValue();
                double val4 = 1.0;
                marshall.updateValues(val1, val2, val3, val4, tid, msgCount, time);
                appender.writeBytes(marshall);
            }
        }
    }

    public void reset() {
        for (Histogram msgHistogram : msgHistograms.values()) {
            msgHistogram.reset();
        }
        allMsgs.reset();
    }

    @Override
    protected boolean isValid(int tid, int schemaID, int version) {
        msgCount++;
        if (msgCount % LOGGING_FREQUENCY == 0 && msgCount > WARMUP_MESSAGES) {
            msgHistograms.forEachInt(consumingLogger);
        }

        if (PERSIST_FREQUENCY > 0 && msgCount > WARMUP_MESSAGES && msgCount % PERSIST_FREQUENCY == 0) {
            persist();
        }

        if (msgCount > WARMUP_MESSAGES && endOfWarmup == 0) {
            endOfWarmup = System.currentTimeMillis();
            long warmupDuration = endOfWarmup - startTime;
            persistWarmupDuration(warmupDuration);
        }

        return true;
    }

    private void persistWarmupDuration(long warmupDuration) {
        // TODO: Find a better way to do stuff like this
        final String warmupDets = warmupDuration + "," + startTime + "," + endOfWarmup;
        logger.info("WARMUP=" + warmupDets);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPrefix + "-PUBLISHER-TIMEOFFSETS.csv"))) {
            writer.write(warmupDets);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    final IntObjConsumer<Histogram> consumingLogger = new IntObjConsumer<Histogram>() {
        @Override
        public void accept(int i, Histogram histogram) {
            double mean = histogram.getMean();
            if (mean > 0) {
                double median = histogram.getValueAtPercentile(0.5);
                logger.info("TID=" + i + ", mean=" + mean + ", median=" + median);
            }
        }
    };

    private Histogram getMsgHistogram(int tid) {
        Histogram histogram = null;

        if (msgHistograms.containsKey(tid)) {
            histogram = msgHistograms.get(tid);
        } else {
            histogram = new Histogram(MAX_BUCKETS, 0);
            msgHistograms.put(tid, histogram);
        }
        return histogram;
    }

    @Override
    public void handleSingleSidedQuote(SingleSidedQuoteDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }

    @Override
    public void handleTwoSidedQuote(TwoSidedQuoteDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }

    @Override
    public void handleMassBidsUpdate(MassBidsUpdateDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }

    @Override
    public void handleMassAsksUpdate(MassAsksUpdateDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }

    @Override
    public void handleMassSidedUpdate(MassSidedUpdateDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }

    @Override
    public void handleGroupedSidedUpdate(GroupedSidedUpdateDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }

    @Override
    public void handleMultiSymbolMassBidsUpdate(
            MultiSymbolMassBidsUpdateDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }

    @Override
    public void handleMultiSymbolMassAsksUpdate(
            MultiSymbolMassAsksUpdateDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }

    @Override
    public void handleMultiSymbolMassSidedUpdate(
            MultiSymbolMassSidedUpdateDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }

    @Override
    public void handleMarketState(MarketStateDecoder msgDecoder, int streamId, int sessionId) {
        long now = System.nanoTime();
        int tid = msgDecoder.sbeTemplateId();
        Histogram h = getMsgHistogram(tid);
        long delta = now - msgDecoder.time();

        if (delta < MAX_BUCKETS && msgCount > WARMUP_MESSAGES) {
            h.recordValue(delta);
            allMsgs.recordValue(delta);
        }
    }
}
