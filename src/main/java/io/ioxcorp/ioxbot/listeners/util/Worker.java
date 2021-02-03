package io.ioxcorp.ioxbot.listeners.util;

import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {
    CountDownLatch latch;

    public Worker(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        // do some job

        // when ready release latch
        latch.countDown();

        System.out.println("Latch Released");
    }
}
