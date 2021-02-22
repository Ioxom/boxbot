package io.ioxcorp.ioxbox.data.format;

import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * simple class to attach a boolean to a MessageChannel.
 * only used for returning both at once in {@link io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter}
 */
public final class WhatAmIDoing {
    private final MessageChannel channel;
    private final boolean result;

    public WhatAmIDoing(final MessageChannel channel, final boolean b) {
        this.channel = channel;
        this.result = b;
    }

    public MessageChannel getChannel() {
        return this.channel;
    }

    public boolean getResult() {
        return this.result;
    }
}
