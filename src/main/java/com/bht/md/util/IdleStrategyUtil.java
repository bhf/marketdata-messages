package com.bht.md.util;

import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;

public class IdleStrategyUtil {

    public static IdleStrategy getIdleStrategy() {
        return new BusySpinIdleStrategy();
    }
}
