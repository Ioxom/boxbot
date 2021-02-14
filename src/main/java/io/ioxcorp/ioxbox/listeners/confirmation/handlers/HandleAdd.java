package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.data.format.WhatAmIDoing;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Random;

public class HandleAdd implements Runnable {
    private final CustomUser user;
    private final CustomUser asker;
    private final MessageChannel initialChannel;

    public HandleAdd(CustomUser user, CustomUser asker, MessageChannel initialChannel) {
        this.user = user;
        this.asker = asker;
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
            asker.getBox().add(user);
            response.getChannel().sendMessage(helper.successEmbed("user accepted, added them to your box")).queue();
        } else {
            Random r = new Random();
            if (r.nextInt(4) == 0) {
                asker.getBox().add(user);
                response.getChannel().sendMessage(new EmbedBuilder()
                        .setDescription("user declined, but you managed to wrestle them in with superior strength")
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(0x00FF00)
                        .setFooter("requested by user " + user.getTag() + "\nbox id: " + EmbedHelper.getBoxID(user))
                        .build()
                ).queue();
            } else {
                response.getChannel().sendMessage(helper.errorEmbed("user refused")).queue();
            }
        }
    }
}