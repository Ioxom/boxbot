package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
import io.ioxcorp.ioxbox.listeners.confirmation.Response;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * opens a new {@link Box} containing a user owned by the the asking user, after asking for confirmation<br>
 * usage: new {@link Thread Thread(HandleOpenWithUser)}{@link Thread#start() .start()}
 * @author ioxom
 */
public final class HandleOpenWithUser extends Handler {
    private final CustomUser askingUser;

    public HandleOpenWithUser(final CustomUser user, final CustomUser inputAskingUser, final MessageChannel initialChannel) {
        super(user, initialChannel);
        this.askingUser = inputAskingUser;
    }

    static final int USER_FORCE_CHANCE = 4;

    @Override
    public void run() {
        final EmbedHelper helper = new EmbedHelper(askingUser);
        if (ConfirmationGetter.gettingConfirmationFrom(getUser().getId())) {
            getInitialChannel().sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        } else {
            getInitialChannel().sendMessage(helper.successEmbed(getUser().getPing() + ", do you want to be added to " + askingUser.getPing() + "'s new box?")).queue();
        }

        final Response response = ConfirmationGetter.crab(getUser().getId(), getInitialChannel());

        if (response == null) {
            getInitialChannel().sendMessage(helper.errorEmbed("confirmation from a user can only be asked for one thing at once, please wait until they've answered the other queries that are waiting on them")).queue();
            return;
        }

        if (Boolean.TRUE.equals(response.getAnswer())) {
            Box.createBox(askingUser, getUser());
            response.getChannel().sendMessage(new EmbedBuilder()
                    .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                    .setImage("https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/gifs/get_in_box.gif")
                    .setTitle("put " + getUser().getPing() + " in your new box!")
                    .setDescription("the user allowed you to put them in the box!")
                    .setColor(EmbedHelper.SUCCESS_EMBED_COLOUR)
                    .setFooter("requested by user " + askingUser.getAsTag() + "\nbox id: " + EmbedHelper.getBoxID(askingUser))
                    .build()
            ).queue();
            Main.FRAME.log(LogType.CMD, "open a new box with user " + getUser().getAsTag(), askingUser);
        } else {
            if (Main.RANDOM.nextInt(USER_FORCE_CHANCE) == 0) {
                Box.createBox(askingUser, getUser());
                response.getChannel().sendMessage(new EmbedBuilder()
                        .setDescription("user declined, but you managed to wrestle them into the box with superior strength")
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(EmbedHelper.SUCCESS_EMBED_COLOUR)
                        .setImage("https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/gifs/get_in_box.gif")
                        .setFooter("requested by user " + askingUser.getAsTag() + "\nbox id: " + EmbedHelper.getBoxID(askingUser))
                        .build()
                ).queue();
                Main.FRAME.log(LogType.CMD, "open a new box with user " + getUser().getAsTag(), askingUser);
            } else if (response.gotProperResponse()) {
                response.getChannel().sendMessage(helper.errorEmbed("user refused")).queue();
            }
        }
    }
}
