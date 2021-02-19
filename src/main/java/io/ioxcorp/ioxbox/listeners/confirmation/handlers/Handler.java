package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.data.format.CustomUser;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Handler implements Runnable {
    public final CustomUser user;
    public final MessageChannel initialChannel;

    public Handler(CustomUser user, MessageChannel channel) {
        this.user = user;
        this.initialChannel = channel;
    }

    @Override
    public void run() {

    }
}