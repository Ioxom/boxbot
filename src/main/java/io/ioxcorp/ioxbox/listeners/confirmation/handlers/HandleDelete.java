package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.data.format.WhatAmIDoing;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
import net.dv8tion.jda.api.entities.MessageChannel;

public class HandleDelete implements Runnable {
    private final CustomUser user;
    private final MessageChannel initialChannel;

    public HandleDelete(CustomUser user, MessageChannel initialChannel) {
        this.user = user;
        this.initialChannel = initialChannel;
    }

    @Override
    public void run() {
        if (ConfirmationGetter.gettingConfirmationFrom(user.id)) {
            initialChannel.sendMessage("confirmation from a user can only be asked for one thing at once, wait until they've answered the other queries that are waiting on them").queue();
            return;
        }

        WhatAmIDoing response = ConfirmationGetter.crab(user.id);
        ConfirmationGetter.clean(user.id);
        EmbedHelper helper = new EmbedHelper(user);
        if (response.getB()) {
            Main.boxes.remove(user.id);
            response.getChannel().sendMessage(helper.successEmbed("successfully deleted your box!")).queue();
        } else {
            response.getChannel().sendMessage(helper.errorEmbed("received false response: did not delete box")).queue();
        }
    }
}