package io.ioxcorp.ioxbox.listeners.status;

import io.ioxcorp.ioxbox.listeners.MainListener;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;

/**
 * when run, swaps the status between statuses[0] and statuses[1]
 */
public class StatusRunnable implements Runnable {
    private int i;
    private final Presence presence;
    private final String[] statuses = {
            "prefix | " + MainListener.prefix,
            "help | " + MainListener.prefix + "commands"
    };

    public StatusRunnable(Presence presence) {
        this.presence = presence;
        this.i = 0;
    }

    @Override
    public void run() {
        if (i == 0) {
            i = 1;
        } else {
            i = 0;
        }
        this.presence.setActivity(Activity.playing(statuses[i]));
    }
}