package io.ioxcorp.ioxbox.data.format;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * simple class to attach a boolean to a MessageChannel
 * only used for returning both at once in {@link io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter}
 */
public class WhatAmIDoing {
    @JsonProperty("channel")
    private final MessageChannel channel;
    @JsonProperty("result")
    private final boolean result;

    public WhatAmIDoing(MessageChannel channel, boolean b) {
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