package io.ioxcorp.ioxbox.listeners;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    //TODO: this shouldn't be static - probably needs conversion to a list
    public static MessageChannel channel;
    public boolean timedOut;

    public ConfirmationGetter(CountDownLatch latch, long id) {
        this.id = id;
        this.latch = latch;
        this.attempts = 0;
    }

    public static ArrayList<Boolean> booleans;
    //TODO: this shouldn't be static - probably needs conversion to a list
    public static int pos;

    //TODO: no
    public static ConfirmationGetter confirmationGetter;
    public static WhatAmIDoing crab(long id) {
        System.out.println("started crab");
        if (booleans == null) {
            pos = 0;
            booleans = new ArrayList<>();
        } else {
            pos = booleans.size();
        }
        booleans.add(pos, false);
        System.out.println("added to list");

        CountDownLatch crabDownLatch = new CountDownLatch(1);
        confirmationGetter = new ConfirmationGetter(crabDownLatch, id);
        new Thread(confirmationGetter).start();

        try {
            crabDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new WhatAmIDoing(channel, booleans.get(pos));
    }

    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        if (this.timedOut) {
            channel.sendMessage("no proper response received, assuming no").queue();
            booleans.add(pos, false);
        } else {
            booleans.add(pos, response);
        }
    }
}
