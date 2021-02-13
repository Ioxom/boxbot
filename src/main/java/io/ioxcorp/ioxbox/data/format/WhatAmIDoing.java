package io.ioxcorp.ioxbox.data.format;

import net.dv8tion.jda.api.entities.MessageChannel;

public class WhatAmIDoing {
    private final MessageChannel channel;
    private final  boolean b;
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