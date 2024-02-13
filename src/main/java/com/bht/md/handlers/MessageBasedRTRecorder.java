package com.bht.md.handlers;

import com.bht.md.messages.*;
import com.bht.md.perfmeta.PerformanceMetaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Track roundtrip latencies for all message types and per message type Persists to a Chronicle
 * Queue or to a text file
 */
public class MessageBasedRTRecorder extends AbstractSBEDecodingHandler {

  final Logger logger = LoggerFactory.getLogger(MessageBasedRTRecorder.class);
  private final int warmupMessages;
  private final long startTime;
  private final PerformanceMetaManager perfTracker;
  int msgCount = 0;
  private long endOfWarmup;

  public MessageBasedRTRecorder(PerformanceMetaManager perfTrack, long startTime, int warmupCount) {
    this.startTime = startTime;
    this.warmupMessages = warmupCount;
    this.perfTracker = perfTrack;
  }

  @Override
  protected boolean isValid(int tid, int schemaID, int version) {
    msgCount++;
    if (msgCount > warmupMessages && endOfWarmup == 0) {
      endOfWarmup = System.currentTimeMillis();
      long warmupDuration = endOfWarmup - startTime;
      persistWarmupDuration(warmupDuration);
    }

    return true;
  }

  private void persistWarmupDuration(long warmupDuration) {
    perfTracker.handleTimeOffsetsMarshall(startTime, endOfWarmup);
  }

  @Override
  public void handleSingleSidedQuote(SingleSidedQuoteDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }

  @Override
  public void handleTwoSidedQuote(TwoSidedQuoteDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }

  @Override
  public void handleMassBidsUpdate(MassBidsUpdateDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }

  @Override
  public void handleMassAsksUpdate(MassAsksUpdateDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }

  @Override
  public void handleMassSidedUpdate(MassSidedUpdateDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }

  @Override
  public void handleGroupedSidedUpdate(
      GroupedSidedUpdateDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }

  @Override
  public void handleMultiSymbolMassBidsUpdate(
      MultiSymbolMassBidsUpdateDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }

  @Override
  public void handleMultiSymbolMassAsksUpdate(
      MultiSymbolMassAsksUpdateDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }

  @Override
  public void handleMultiSymbolMassSidedUpdate(
      MultiSymbolMassSidedUpdateDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }

  @Override
  public void handleMarketState(MarketStateDecoder msgDecoder, int stream, int session) {
    if (endOfWarmup > 0) {
      long now = System.nanoTime() - msgDecoder.time();
      long delta = now - msgDecoder.time();
      int tid = msgDecoder.sbeTemplateId();
      perfTracker.handleMsgRT(tid, stream, session, delta);
    }
  }
}
