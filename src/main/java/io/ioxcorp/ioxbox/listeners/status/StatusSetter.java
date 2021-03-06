package io.ioxcorp.ioxbox.listeners.status;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * a simple {@link net.dv8tion.jda.api.hooks.EventListener} that swaps statuses every 20 seconds
 * @author ioxom
 */
public final class StatusSetter extends ListenerAdapter {
    private final int timeBetweenChanges;
    public StatusSetter(final int timeBetweenChanges) {
        this.timeBetweenChanges = timeBetweenChanges;
    }

    @Override
    public void onReady(final ReadyEvent event) {
        //when JDA is ready, set our status at the interval of timeBetweenChanges
        StatusRunnable setStatus = new StatusRunnable(
                event.getJDA().getPresence(),
                null,
                this.timeBetweenChanges);
        setStatus.start();
    }
}
