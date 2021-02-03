package io.ioxcorp.ioxbox.listeners.util;

import java.util.concurrent.CountDownLatch;

public class ConditionAwaiter {
    CountDownLatch latch;

    public ConditionAwaiter(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // log interrupt
            System.out.println("Interrupted");
        }
        System.out.println("Unblocked");
    }
}
