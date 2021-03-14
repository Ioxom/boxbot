package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.data.json.JacksonYeehawHelper;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.utils.tuple.Pair;

/**
 * deletes a user's box, after asking for confirmation <br>
 * usage: new {@link Thread Thread(HandleDelete)}{@link Thread#start() .start()}
 * @author ioxom
 */
public final class HandleDelete extends Handler {

    public HandleDelete(final CustomUser user, final MessageChannel initialChannel) {
        super(user, initialChannel);
    }

    @Override
    public void run() {
        final EmbedHelper helper = new EmbedHelper(getUser());
        if (ConfirmationGetter.gettingConfirmationFrom(getUser().getId())) {
            getInitialChannel().sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, wait until they've answered the other queries that are waiting on them")).queue();
            return;
        } else {
            getInitialChannel().sendMessage(helper.successEmbed("delete box? this action is permanent and will remove everything in your box")).queue();
        }

        final Pair<MessageChannel, Boolean> response = ConfirmationGetter.crab(getUser().getId(), getInitialChannel());

        if (response == null) {
            getInitialChannel().sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        }

        if (response.getRight()) {
            Main.BOXES.remove(getUser().getId());
            JacksonYeehawHelper.save();
            response.getLeft().sendMessage(helper.successEmbed("successfully deleted your box!")).queue();
            Main.FRAME.log(LogType.CMD, "delete box", getUser());
        } else {
            response.getLeft().sendMessage(helper.errorEmbed("received false response: did not delete box")).queue();
        }
    }
}
