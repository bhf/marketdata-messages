package com.bht.md.util;

import java.util.concurrent.ThreadFactory;
import net.openhft.affinity.AffinityLock;

public class ThreadFactoryUtil {

    public static ThreadFactory getThreadFactory(String name, int cpuId) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {

                Thread t = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try (AffinityLock al = AffinityLock.acquireLock(cpuId)) {
                                    r.run();
                                }
                            }
                        },
                        name);
                return t;
            }
        };
    }
}
