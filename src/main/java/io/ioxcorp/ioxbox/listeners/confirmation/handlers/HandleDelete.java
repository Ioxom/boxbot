package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.data.format.WhatAmIDoing;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * deletes a user's box, after asking for confirmation <br>
 * usage: new {@link Thread}(instance of {@link HandleDelete}).start();
 * @author ioxom
 */
public final class HandleDelete extends Handler {

    public HandleDelete(final CustomUser user, final MessageChannel initialChannel) {
        super(user, initialChannel);
    }

    @Override
    public void run() {
        EmbedHelper helper = new EmbedHelper(this.getUser());
        if (ConfirmationGetter.gettingConfirmationFrom(this.getUser().getId())) {
            this.getInitialChannel().sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, wait until they've answered the other queries that are waiting on them")).queue();
            return;
        } else {
            this.getInitialChannel().sendMessage(helper.successEmbed("delete box? this action is permanent and will remove everything in your box")).queue();
        }

        WhatAmIDoing response = ConfirmationGetter.crab(this.getUser().getId(), this.getInitialChannel());

        if (response == null) {
            this.getInitialChannel().sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        }

        if (response.getResult()) {
            Main.BOXES.remove(this.getUser().getId());
            response.getChannel().sendMessage(helper.successEmbed("successfully deleted your box!")).queue();
            Main.FRAME.log(LogType.CMD, "delete box", this.getUser());
        } else {
            response.getChannel().sendMessage(helper.errorEmbed("received false response: did not delete box")).queue();
        }
    }
}
