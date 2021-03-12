package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.utils.tuple.Pair;

/**
 * adds a user to another user's box, after asking for confirmation<br>
 * usage: new {@link Thread Thread(HandleAdd)}{@link Thread#start() .start()}
 * @author ioxom, thonkman
 */
public final class HandleAdd extends Handler {
    private final CustomUser askingUser;

    public HandleAdd(final CustomUser user, final CustomUser askingUser, final MessageChannel initialChannel) {
        super(user, initialChannel);
        this.askingUser = askingUser;
    }

    @Override
    public void run() {
        final EmbedHelper helper = new EmbedHelper(askingUser);
        if (ConfirmationGetter.gettingConfirmationFrom(getUser().getId())) {
            this.getInitialChannel().sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        } else {
            this.getInitialChannel().sendMessage(helper.successEmbed(getUser().getPing() + ", do you give " + askingUser.getPing() + " permission to put you in their box?")).queue();
        }

        final Pair<MessageChannel, Boolean> response = ConfirmationGetter.crab(this.getUser().getId(), this.getInitialChannel());

        if (response == null) {
            this.getInitialChannel().sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        }

        if (response.getRight()) {
            this.askingUser.getBox().add(this.getUser());
            response.getLeft().sendMessage(new EmbedBuilder()
                    .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                    .setImage("https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/gifs/get_in_box.gif")
                    .setTitle("caught!")
                    .setDescription("the user allowed you to put them in the box!")
                    .setColor(EmbedHelper.SUCCESS_EMBED_COLOUR)
                    .setFooter("requested by user " + this.askingUser.getAsTag() + "\nbox id: " + EmbedHelper.getBoxID(this.askingUser))
                    .build()
            ).queue();
            Main.FRAME.log(LogType.CMD, "add " + this.getUser().getAsTag() + " to their box", this.askingUser);
        } else {
            if (Main.RANDOM.nextInt(4) == 0) {
                this.askingUser.getBox().add(this.getUser());
                response.getLeft().sendMessage(new EmbedBuilder()
                        .setDescription("user declined, but you managed to wrestle them in with superior strength")
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(EmbedHelper.SUCCESS_EMBED_COLOUR)
                        .setImage("https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/gifs/get_in_box.gif")
                        .setFooter("requested by user " + this.askingUser.getAsTag() + "\nbox id: " + EmbedHelper.getBoxID(this.askingUser))
                        .build()
                ).queue();
                Main.FRAME.log(LogType.CMD, "add " + this.getUser().getAsTag() + " to their box", this.askingUser);
            } else {
                response.getLeft().sendMessage(helper.errorEmbed("user refused")).queue();
            }
        }
    }
}
