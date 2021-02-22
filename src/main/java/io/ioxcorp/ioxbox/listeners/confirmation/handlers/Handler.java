package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.data.format.CustomUser;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Handler implements Runnable {
    private final CustomUser user;
    private final MessageChannel initialChannel;

    public Handler(final CustomUser user, final MessageChannel channel) {
        this.user = user;
        this.initialChannel = channel;
    }

    @Override
    public void run() {

    }

    public CustomUser getUser() {
        return this.user;
    }

    public MessageChannel getInitialChannel() {
        return this.initialChannel;
    }
}
