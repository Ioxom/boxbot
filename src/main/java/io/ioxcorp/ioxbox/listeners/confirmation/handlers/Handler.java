package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.data.format.CustomUser;
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * base class for handling confirmation requests<br>
 * implements {@link Runnable} - should be used by running on a new thread
 * @author ioxom
 */
public class Handler implements Runnable {
    private final CustomUser user;
    private final MessageChannel initialChannel;

    public Handler(final CustomUser user, final MessageChannel channel) {
        this.user = user;
        this.initialChannel = channel;
    }

    @Override
    public void run() {
        //this does nothing, meant to be used only when Handler is extended
    }

    /**
     * gets the user object
     * @return a {@link CustomUser} being handled
     */
    public CustomUser getUser() {
        return this.user;
    }

    /**
     * gets the initial channel object
     * @return a {@link MessageChannel} object where we originally got the confirmation request
     */
    public MessageChannel getInitialChannel() {
        return this.initialChannel;
    }
}
