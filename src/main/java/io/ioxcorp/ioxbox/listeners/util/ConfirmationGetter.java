package io.ioxcorp.ioxbox.listeners.util;

import io.ioxcorp.ioxbox.data.format.WhatAmIDoing;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class ConfirmationGetter extends ListenerAdapter implements Runnable {
    // CountDownLatch latch = new CountDownLatch(1);
    // ConfirmationGetter g = new ConfirmationGetter(latch, 6L);
    // new Thread(g).start();
    //
    // should be used like this

    public final CountDownLatch latch;
    public final long id;
    public boolean response;
    public int attempts;
    public static HashMap<Long, MessageChannel> channels = new HashMap<>();
    public boolean timedOut;

    public ConfirmationGetter(CountDownLatch latch, long id) {
        this.id = id;
        this.latch = latch;
        this.attempts = 0;
    }

    public static HashMap<Long, Boolean> booleans;

    public static HashMap<Long, ConfirmationGetter> confirmationGetters;
    public static WhatAmIDoing crab(long id) {
        System.out.println("started crab");
        if (booleans == null) {
            booleans = new HashMap<>();
        }
        if (confirmationGetters == null) confirmationGetters = new HashMap<>();
        booleans.put(id, false);
        System.out.println("added to list");

        CountDownLatch crabDownLatch = new CountDownLatch(1);
        confirmationGetters.put(id, new ConfirmationGetter(crabDownLatch, id));
        new Thread(confirmationGetters.get(id)).start();

        try {
            crabDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new WhatAmIDoing(channels.get(id), booleans.get(id));
    }

    public static void clean(long id) {
        booleans.remove(id);
        channels.remove(id);
        confirmationGetters.remove(id);
    }

    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        if (this.timedOut) {
            channels.get(this.id).sendMessage("no proper response received, assuming no").queue();
            booleans.put(this.id, false);
        } else {
            booleans.put(this.id, response);
        }
    }
}