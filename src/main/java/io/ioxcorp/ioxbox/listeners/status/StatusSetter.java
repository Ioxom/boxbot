package io.ioxcorp.ioxbox.listeners.status;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * a simple {@link net.dv8tion.jda.api.hooks.EventListener} that swaps statuses every 20 seconds
 * @author ioxom
 */
public final class StatusSetter extends ListenerAdapter {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final int timeBetweenChanges;
    public StatusSetter(final int timeBetweenChanges) {
        this.timeBetweenChanges = timeBetweenChanges;
    }

    @Override
    public void onReady(final ReadyEvent event) {
        //when JDA is ready, set our status every 20 seconds
        StatusRunnable setStatus = new StatusRunnable(event.getJDA().getPresence());
        scheduler.scheduleAtFixedRate(setStatus, 0, this.timeBetweenChanges, SECONDS);
    }
}
