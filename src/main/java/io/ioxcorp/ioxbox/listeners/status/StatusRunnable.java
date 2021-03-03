package io.ioxcorp.ioxbox.listeners.status;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * when run, moves the status to the next one in the array sent as a parameter
 * @author ioxom
 */
public final class StatusRunnable implements Runnable {
    private int i;
    private final Presence presence;
    private final String[] statuses;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final int timeBetweenChanges;

    public StatusRunnable(final Presence jdaPresence, final String[] statuses, final int timeBetweenChanges) {
        this.timeBetweenChanges = timeBetweenChanges;
        this.statuses = statuses;
        this.presence = jdaPresence;
        this.i = 0;
    }

    @Override
    public void run() {
        if (i == statuses.length - 1) {
            i = 0;
        } else {
            i ++;
        }
        this.presence.setActivity(Activity.playing(statuses[i]));
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this, 0, this.timeBetweenChanges, SECONDS);
    }
}
