package io.ioxcorp.ioxbox.listeners.status;

import io.ioxcorp.ioxbox.Main;
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
    private String[] statuses;
    private final boolean useDefaultStatuses;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final int timeBetweenChanges;

    /**
     * create a new {@link StatusRunnable}
     * @param jdaPresence the {@link Presence} to update
     * @param inputStatuses the statuses to cycle through; if null uses default statuses
     * @param time the time in seconds between cycles
     */
    public StatusRunnable(final Presence jdaPresence, final String[] inputStatuses, final int time) {
        this.timeBetweenChanges = time;
        this.useDefaultStatuses = inputStatuses == null;
        this.statuses = inputStatuses;
        this.presence = jdaPresence;
        this.i = 0;
    }

    @Override
    public void run() {
        if (useDefaultStatuses) {
            statuses = new String[] {
                    "prefix | " + Main.getConfig().getPrefix(),
                    "help | " + Main.getConfig().getPrefix() + "commands"
            };
        }

        if (i == statuses.length - 1) {
            i = 0;
        } else {
            i++;
        }

        this.presence.setActivity(Activity.playing(statuses[i]));
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this, 0, this.timeBetweenChanges, SECONDS);
    }
}
