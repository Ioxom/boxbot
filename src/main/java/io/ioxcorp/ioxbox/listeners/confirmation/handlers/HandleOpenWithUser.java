package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.data.format.WhatAmIDoing;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Random;

/**
 * opens a new {@link Box} containing a user owned by the the asking user, after asking for confirmation<br>
 * usage: new {@link Thread}(instance of {@link HandleOpenWithUser}).start();
 * @author ioxom
 */
public class HandleOpenWithUser implements Runnable {
    private final CustomUser user;
    private final CustomUser askingUser;
    private final MessageChannel initialChannel;

    public HandleOpenWithUser(CustomUser user, CustomUser askingUser, MessageChannel initialChannel) {
        this.user = user;
        this.askingUser = askingUser;
        this.initialChannel = initialChannel;
    }

    @Override
    public void run() {
        EmbedHelper helper = new EmbedHelper(user);
        if (ConfirmationGetter.gettingConfirmationFrom(user.id)) {
            initialChannel.sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        }

        WhatAmIDoing response = ConfirmationGetter.crab(user.id);

        if (response == null) {
            this.initialChannel.sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        }

        if (response.getB()) {
            Box.createBox(askingUser, user);
            response.getChannel().sendMessage(new EmbedBuilder()
                    .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                    .setImage("https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/gifs/get_in_box.gif")
                    .setTitle("put " + user.getPing() + " in your new box!")
                    .setDescription("the user allowed you to put them in the box!")
                    .setColor(0x00ff00)
                    .setFooter("requested by user " + askingUser.getTag() + "\nbox id: " + EmbedHelper.getBoxID(askingUser))
                    .build()
            ).queue();
        } else {
            Random r = new Random();
            if (r.nextInt(4) == 0) {
                Box.createBox(askingUser, user);
                response.getChannel().sendMessage(new EmbedBuilder()
                        .setDescription("user declined, but you managed to wrestle them into the box with superior strength")
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(0x00ff00)
                        .setImage("https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/gifs/get_in_box.gif")
                        .setFooter("requested by user " + askingUser.getTag() + "\nbox id: " + EmbedHelper.getBoxID(askingUser))
                        .build()
                ).queue();
            } else {
                response.getChannel().sendMessage(helper.errorEmbed("user refused")).queue();
            }
        }
    }
}
