package io.ioxcorp.ioxbox.listeners;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.helpers.MessageHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
import io.ioxcorp.ioxbox.listeners.confirmation.handlers.HandleAdd;
import io.ioxcorp.ioxbox.listeners.confirmation.handlers.HandleDelete;
import io.ioxcorp.ioxbox.listeners.confirmation.handlers.HandleOpenWithUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;

import static io.ioxcorp.ioxbox.Main.FRAME;
import static io.ioxcorp.ioxbox.Main.getConfig;

/**
 * the main listener for ioxbox<br>
 * executes commands from text channels
 * @author ioxom
 * @author thonkman
 */
public final class MainListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {

        final String prefix = Main.getConfig().getPrefix();
        final Message eventMessage = event.getMessage();
        final String messageContentRaw = eventMessage.getContentRaw().toLowerCase();

        if (!messageContentRaw.startsWith(prefix) || event.getAuthor().isBot()) {
            return;
        }

        final String[] messageContent = messageContentRaw.split(prefix)[1].split(" ");
        final CustomUser author = new CustomUser(event.getAuthor());
        final EmbedHelper helper = new EmbedHelper(author);
        final MessageChannel channel = eventMessage.getChannel();

        switch (messageContent[0]) {
            case "help":
                EmbedBuilder helpEmbed = new EmbedBuilder()
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(helper.getRandomEmbedColour())
                        .addField("what in the heck does this bot do?", "this bot is very hot, it stores cool things for you. like words, words and more words for now.", false)
                        .addField("commands", "use " + prefix + "commands for a list", false)
                        .addField("ioxcorp™ inc", "ioxcorp™ inc. was founded in 04/01/20 by ioxom. it is also maintained by thonkman.", false)
                        .setFooter("powered by ioxcorp™");
                channel.sendMessage(helpEmbed.build()).queue();
                FRAME.log(LogType.CMD, "help", author);
                break;

            case "commands":
            case "cmds":
                EmbedBuilder commandEmbed = new EmbedBuilder()
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(EmbedHelper.SUCCESS_EMBED_COLOUR)
                        .addField("add", "adds a user or item to your box. if you don't have one, creates a new box for you.\nsyntax: `" + prefix + "add [ping or item]`", false)
                        .addField("remove", "removes a user or item from your box. if you have no box this will error.\nsyntax: `" + prefix + "remove [ping or item]`", false)
                        .addField("ping", "checks the bot's ping is ms.\nsyntax: `" + prefix + "ping`", false)
                        .addField("open", "opens a new box for you, with items if specified.\nsyntax:\n`" + prefix + "open`,\n`" + prefix + "open [ping or item]`", false)
                        .addField("delete", "deletes your box from our files permanently.\nsyntax: `" + prefix + "delete`", false)
                        .addField("list", "shows you all users and items in your or another user's box.\nsyntax:\n`" + prefix + "list`,\n`" + prefix + "list [ping]`", false)
                        .addField("commands", "lists ioxbox's available commands.\nsyntax: `" + prefix + "commands`", false)
                        .addField("help", "general information about ioxbox.\nsyntax: `" + prefix + "help`", false);
                channel.sendMessage(commandEmbed.build()).queue();
                FRAME.log(LogType.CMD, "commands", author);
                break;

            case "add":
                if (/* check for mentioned users */ eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
                    if (author.hasBox()) {
                        HandleAdd yes = new HandleAdd(user, author, channel);
                        ConfirmationGetter.EXECUTOR.submit(yes);
                        break;
                    } else {
                        channel.sendMessage(helper.errorEmbed("you have no box to add to:\nwhy not open with " + prefix + "open?")).queue();
                    }
                } else if (/* we need an item to add */ messageContent.length > 1) {
                    if (author.hasBox()) {
                        author.getBox().add(messageContent[1]);
                        channel.sendMessage(helper.successEmbed(
                                "successfully added item to box!",
                                "items:\n" + author.getBox().itemsToString()
                        )).queue();
                    } else {
                        Box.createBox(author, messageContent[1]);
                        channel.sendMessage(helper.successEmbed("box successfully created with item " + messageContent[1] + "!")).queue();
                    }
                }
                FRAME.log(LogType.CMD, prefix + "add", author);
                break;

            case "remove":
                if (!author.hasBox()) {
                    channel.sendMessage(helper.errorEmbed("error removing from box: this box does not exist")).queue();
                    FRAME.log(LogType.CMD, prefix + "remove", author);
                    break;
                }

                if (eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
                    if (author.getBox().contains(user)) {
                        author.getBox().remove(user);
                        channel.sendMessage(helper.successEmbed(
                                "successfully removed user from box!",
                                "users:\n" + author.getBox().usersToString()
                        )).queue();
                    } else {
                        channel.sendMessage(helper.errorEmbed("error removing from box: user's box does not contain the requested user")).queue();
                    }
                } else if (/* we need an item to remove*/ messageContent.length > 1) {
                    if (author.getBox().contains(messageContent[1])) {
                        author.getBox().remove(messageContent[1]);
                        channel.sendMessage(helper.successEmbed(
                                "successfully removed item from box!",
                                "items:\n" + author.getBox().itemsToString()
                        )).queue();
                    } else {
                        channel.sendMessage(helper.errorEmbed("error removing from box: box does not exist or does not contain the requested item")).queue();
                    }
                } else {
                    channel.sendMessage(helper.errorEmbed("error removing from box: nothing found to remove in message")).queue();
                }
                FRAME.log(LogType.CMD, prefix + "remove", author);
                break;

            case "open":
                if (!eventMessage.getMentionedUsers().isEmpty()) {
                    CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
                    HandleOpenWithUser handleOpenWithUser = new HandleOpenWithUser(user, author, channel);
                    ConfirmationGetter.EXECUTOR.submit(handleOpenWithUser);
                    break;
                } else {
                    if (messageContent.length == 1) {
                        createBox(author, null, helper, channel);
                    } else {
                        createBox(author, messageContent[1], helper, channel);
                    }
                }
                FRAME.log(LogType.CMD, prefix + "open", author);
                break;

            case "delete":
                if (author.hasBox()) {
                    final HandleDelete yes = new HandleDelete(author, channel);
                    ConfirmationGetter.EXECUTOR.submit(yes);
                    break;
                } else {
                    channel.sendMessage(helper.errorEmbed("no box found to remove")).queue();
                }

                FRAME.log(LogType.CMD, prefix + "add", author);
                break;

            case "list":
                if (/* check for users */ eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
                    if (user.hasBox()) {
                        event.getChannel().sendMessage(user.getBox().embed()).queue();
                    } else {
                        event.getChannel().sendMessage(helper.errorEmbed("this user doesn't seem to have a box. they can try opening a new one with " + prefix + "open!")).queue();
                    }
                } else {
                    if (author.hasBox()) {
                        event.getChannel().sendMessage(author.getBox().embed()).queue();
                    } else {
                        event.getChannel().sendMessage(helper.errorEmbed("you don't seem to have a box. try opening a new one with " + prefix + "open!")).queue();
                    }
                }
                FRAME.log(LogType.CMD, prefix + "add", author);
                break;

            case "ping":
                long time = System.currentTimeMillis();
                channel.sendMessage(helper.successEmbed("calculating ping...")).queue(message ->
                        message.editMessage(helper.successEmbed("ioxbox's ping is: " + (System.currentTimeMillis() - time) + "ms")).queue());
                FRAME.log(LogType.CMD, "ping", author);
                break;

            //thonkman why
            case "pickup":
                channel.sendMessage(PICKUPS[Main.RANDOM.nextInt(PICKUPS.length)]).queue();
                break;

            case "cfg":
            case "config":
                if (getConfig().getAdmins().contains(author.getId())) {
                    if (messageContent.length > 2) {
                        switch (messageContent[1]) {
                            case "admin":
                            case "admins":
                                if (messageContent[2].equals("add") && messageContent.length > 3) {
                                    try {
                                        getConfig().getAdmins().add(Long.parseLong(messageContent[3]));
                                        channel.sendMessage(helper.successEmbed("added admin " + MessageHelper.getAsPing(messageContent[3]))).queue();
                                    } catch (NumberFormatException e) {
                                        channel.sendMessage(helper.errorEmbed("failed to add admin: id is invalid")).queue();
                                    }
                                } else if (messageContent[2].equals("remove") && messageContent.length > 3) {
                                    try {
                                        getConfig().getAdmins().remove(Long.parseLong(messageContent[3]));
                                        channel.sendMessage(helper.successEmbed("removed admin " + MessageHelper.getAsPing(messageContent[3]))).queue();
                                    } catch (NumberFormatException e) {
                                        channel.sendMessage(helper.errorEmbed("failed to remove admin: id is invalid")).queue();
                                    }
                                } else {
                                    channel.sendMessage(helper.errorEmbed("not enough arguments or unknown command")).queue();
                                }
                                break;
                            case "prefix":
                                if (messageContent[2].equals("set") && messageContent.length > 3) {
                                    final String newPrefix = messageContent.length > 4 && messageContent[4].equals("-addspace") ? " " : "" + messageContent[3];
                                    getConfig().setPrefix(newPrefix);
                                    channel.sendMessage(helper.successEmbed("set prefix to [" + newPrefix + "]")).queue();
                                } else {
                                    channel.sendMessage(helper.errorEmbed("not enough arguments or unknown command")).queue();
                                }
                                break;
                            case "logcommands":
                                final boolean log = Boolean.parseBoolean(messageContent[2]);
                                getConfig().setCommandLogging(log);
                                channel.sendMessage(helper.successEmbed("set logCommands to " + log)).queue();
                                break;
                        }
                    } else {
                        channel.sendMessage(helper.errorEmbed("not enough arguments")).queue();
                    }
                } else {
                    channel.sendMessage(helper.errorEmbed("you do not have sufficient permissions to edit configuration")).queue();
                }
        }
    }

    private static final String[] PICKUPS = {
            "are you a time traveler?\nbecause I see you in my future ;)",
            "you remind me of my pinkie toe,\nlittle, cute and I\"ll probably bang you on the coffee table later tonight.",
            "there must be a lightswitch on my forehead,\nbecause everytime I see you, you turn me on.",
            "somethings wrong with my eye\"s,\nbecause I can\"t take them off you.",
            "somebody better call God\nbecause he\"s missing an angel.",
            "you should be called wifi,\nbecause I\"m starting to feel a real connection.",
            "I would never play hide and seek with you,\nbecause someone like you is hard to find (;",
            "hershey\"s makes millions of kisses a day...\nall I\"m asking for is one from you.",
            "if i told you that you had a great body, would you hold it against me?"
    };

    public void createBox(final CustomUser owner, final String content, final EmbedHelper helper, final MessageChannel channel) {
        try {
            if (content == null || content.isEmpty()) {
                Box.createBox(owner);
                channel.sendMessage(helper.successEmbed("empty box successfully created!")).queue();
            } else {
                Box.createBox(owner, content);
                channel.sendMessage(helper.successEmbed("box successfully created with item " + content + "!")).queue();
            }
        } catch (InvalidParameterException e) {
            channel.sendMessage(helper.errorEmbed("you seem to already have a box.")).queue();
        } catch (IllegalArgumentException e) {
            channel.sendMessage(helper.errorEmbed(e + ": the object passed to Box#createBox(Object, Object) was of an incompatible type")).queue();
        }
    }
}
