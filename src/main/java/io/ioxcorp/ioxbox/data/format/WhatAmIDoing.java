package io.ioxcorp.ioxbox.data.format;

import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * simple class to attach a boolean to a MessageChannel
 * only used for returning both at once in {@link io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter}
 */
public class WhatAmIDoing {
    private final MessageChannel channel;
    private final boolean b;
    public WhatAmIDoing(MessageChannel channel, boolean b) {
        this.channel = channel;
        this.b = b;
    }

    public MessageChannel getChannel() {
        return this.channel;
    }

    public boolean getB() {
        return this.b;
    }
}