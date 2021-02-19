package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.data.format.WhatAmIDoing;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * opens a new {@link Box} containing a user owned by the the asking user, after asking for confirmation<br>
 * usage: new {@link Thread}(instance of {@link HandleOpenWithUser}).start();
 * @author ioxom
 */
public class HandleOpenWithUser extends Handler {
    private final CustomUser askingUser;

    public HandleOpenWithUser(CustomUser user, CustomUser askingUser, MessageChannel initialChannel) {
        super(user, initialChannel);
        this.askingUser = askingUser;
    }

    @Override
    public void run() {
        EmbedHelper helper = new EmbedHelper(this.user);
        if (ConfirmationGetter.gettingConfirmationFrom(this.user.id)) {
            initialChannel.sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        }

        WhatAmIDoing response = ConfirmationGetter.crab(this.user.id);

        if (response == null) {
            this.initialChannel.sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        }

        if (response.getResult()) {
            Box.createBox(this.askingUser, this.user);
            response.getChannel().sendMessage(new EmbedBuilder()
                    .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                    .setImage("https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/gifs/get_in_box.gif")
                    .setTitle("put " + user.getPing() + " in your new box!")
                    .setDescription("the user allowed you to put them in the box!")
                    .setColor(0x00ff00)
                    .setFooter("requested by user " + this.askingUser.getTag() + "\nbox id: " + EmbedHelper.getBoxID(this.askingUser))
                    .build()
            ).queue();
            Main.frame.log(LogType.CMD, "open a new box with user " + this.user.getTag(), this.askingUser);
        } else {
            if (Main.random.nextInt(4) == 0) {
                Box.createBox(this.askingUser, user);
                response.getChannel().sendMessage(new EmbedBuilder()
                        .setDescription("user declined, but you managed to wrestle them into the box with superior strength")
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(0x00ff00)
                        .setImage("https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/gifs/get_in_box.gif")
                        .setFooter("requested by user " + this.askingUser.getTag() + "\nbox id: " + EmbedHelper.getBoxID(this.askingUser))
                        .build()
                ).queue();
                Main.frame.log(LogType.CMD, "open a new box with user " + user.getTag(), this.askingUser);
            } else {
                response.getChannel().sendMessage(helper.errorEmbed("user refused")).queue();
            }
        }
    }
}
