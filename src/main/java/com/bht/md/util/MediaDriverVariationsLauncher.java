package com.bht.md.util;

import static org.agrona.SystemUtil.loadPropertiesFiles;

import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.NoOpIdleStrategy;
import org.agrona.concurrent.ShutdownSignalBarrier;

public class MediaDriverVariationsLauncher {

    public static void main(String[] args) {

        int version = Integer.parseInt(args[0]);

        switch (version) {
            case 0: {
                launchBasicMediaDriver(new String[] {});
                break;
            }
            case 1: {
                int cpuId = Integer.parseInt(args[1]);
                launchAffinedSingleThreaded(cpuId);
                break;
            }
            case 2: {
                int conductorCpuId = Integer.parseInt(args[1]);
                int receiverCpuId = Integer.parseInt(args[2]);
                int senderCpuId = Integer.parseInt(args[3]);
                launchAffinedLowLatency(conductorCpuId, receiverCpuId, senderCpuId);
                break;
            }
            default:
        }

        launchBasicMediaDriver(args);
    }

    public static void launchBasicMediaDriver(String[] args) {
        loadPropertiesFiles(args);

        final ShutdownSignalBarrier barrier = new ShutdownSignalBarrier();
        final MediaDriver.Context ctx = new MediaDriver.Context().terminationHook(barrier::signal);

        try (MediaDriver ignore = MediaDriver.launch(ctx)) {
            barrier.await();
        }
    }

    public static void launchAffinedLowLatency(int conductorCpuId, int receiverCpuId, int senderCpuId) {
        final MediaDriver.Context ctx = new MediaDriver.Context()
                .termBufferSparseFile(false)
                .useWindowsHighResTimer(true)
                .threadingMode(ThreadingMode.DEDICATED)
                .conductorIdleStrategy(BusySpinIdleStrategy.INSTANCE)
                .receiverIdleStrategy(NoOpIdleStrategy.INSTANCE)
                .senderIdleStrategy(NoOpIdleStrategy.INSTANCE);

        if (conductorCpuId > 0) {
            affineConductor(ctx, conductorCpuId);
        }
        if (receiverCpuId > 0) {
            affineReceiver(ctx, receiverCpuId);
        }
        if (senderCpuId > 0) {
            affineSender(ctx, senderCpuId);
        }

        try (MediaDriver ignored = MediaDriver.launch(ctx)) {
            new ShutdownSignalBarrier().await();
        }
    }

    public static void launchAffinedSingleThreaded(int sharedNetworkCpuId) {
        final MediaDriver.Context ctx = new MediaDriver.Context().threadingMode(ThreadingMode.SHARED_NETWORK);

        /*final MediaDriver.Context ctx = new MediaDriver.Context()
        .termBufferSparseFile(false)
        .useWindowsHighResTimer(true)
        .threadingMode(ThreadingMode.SHARED_NETWORK)
        .conductorIdleStrategy(BusySpinIdleStrategy.INSTANCE)
        .receiverIdleStrategy(BusySpinIdleStrategy.INSTANCE)
        .senderIdleStrategy(BusySpinIdleStrategy.INSTANCE);*/

        if (sharedNetworkCpuId > 0) {
            affineSharedNetwork(ctx, sharedNetworkCpuId);
        }

        try (MediaDriver ignored = MediaDriver.launch(ctx)) {
            new ShutdownSignalBarrier().await();
        }
    }

    private static void affineConductor(MediaDriver.Context ctx, int conductorCpuId) {
        ctx.conductorThreadFactory(ThreadFactoryUtil.getThreadFactory("Conductor", conductorCpuId));
    }

    private static void affineReceiver(MediaDriver.Context ctx, int receiverCpuId) {
        ctx.receiverThreadFactory(ThreadFactoryUtil.getThreadFactory("Aeron Receiver", receiverCpuId));
    }

    private static void affineSender(MediaDriver.Context ctx, int senderCpuId) {
        ctx.senderThreadFactory(ThreadFactoryUtil.getThreadFactory("Aeron Sender", senderCpuId));
    }

    private static void affineSharedNetwork(MediaDriver.Context ctx, int sharedNetworkCpuId) {
        ctx.sharedNetworkThreadFactory(ThreadFactoryUtil.getThreadFactory("Aeron SN", sharedNetworkCpuId));
    }
}
