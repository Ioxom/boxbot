package io.ioxcorp.ioxbox.listeners.confirmation;

import io.ioxcorp.ioxbox.data.format.WhatAmIDoing;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class ConfirmationGetter extends ListenerAdapter implements Runnable {

    public final CountDownLatch latch;
    public final long id;
    public boolean response;
    public int attempts;
    public boolean timedOut;

    public ConfirmationGetter(CountDownLatch latch, long id) {
        this.id = id;
        this.latch = latch;
        this.attempts = 0;
    }

    public static boolean gettingConfirmationFrom(long id) {
        return booleans.containsKey(id) || confirmationGetters.containsKey(id) || channels.containsKey(id);
    }

    public static final HashMap<Long, MessageChannel> channels = new HashMap<>();
    public static final HashMap<Long, Boolean> booleans = new HashMap<>();
    public static final HashMap<Long, ConfirmationGetter> confirmationGetters = new HashMap<>();

    public static WhatAmIDoing crab(long id) {
        if (gettingConfirmationFrom(id)) return new WhatAmIDoing(channels.get(id), false);

        CountDownLatch crabDownLatch = new CountDownLatch(1);
        confirmationGetters.put(id, new ConfirmationGetter(crabDownLatch, id));
        new Thread(confirmationGetters.get(id)).start();

        try {
            crabDownLatch.await();
            return new WhatAmIDoing(channels.get(id), booleans.get(id));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new WhatAmIDoing(channels.get(id), false);
        } finally {
            ConfirmationGetter.clean(id);
        }
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
            e.printStackTrace();
        }

        if (this.timedOut) {
            channels.get(this.id).sendMessage("no proper response received, assuming no").queue();
            booleans.put(this.id, false);
        } else {
            booleans.put(this.id, response);
        }
    }
}