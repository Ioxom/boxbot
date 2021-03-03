package io.ioxcorp.ioxbox.listeners;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
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

/**
 * the main listener for ioxbox<br>
 * executes commands from text channels
 */
public final class MainListener extends ListenerAdapter {
    public static final String PREFIX = "-box ";

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {

        final Message eventMessage = event.getMessage();
        final String messageContentRaw = eventMessage.getContentRaw().toLowerCase();

        if (!messageContentRaw.startsWith(PREFIX) || event.getAuthor().isBot()) {
            return;
        }

        final String[] messageContent = messageContentRaw.split(PREFIX)[1].split(" ");
        final CustomUser author = new CustomUser(event.getAuthor());
        final EmbedHelper helper = new EmbedHelper(author);
        final MessageChannel channel = eventMessage.getChannel();

        switch (messageContent[0]) {
            case "help":

                EmbedBuilder helpEmbed = new EmbedBuilder()
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(helper.getRandomEmbedColour())
                        .addField("what in the heck does this bot do?", "this bot is very hot, it stores cool things for you. like words, words and more words for now.", false)
                        .addField("commands", "use " + PREFIX + "commands for a list", false)
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
                        .addField("add", "adds a user or item to your box. if you don't have one, creates a new box for you.\nsyntax: `" + PREFIX + "add [ping or item]`", false)
                        .addField("remove", "removes a user or item from your box. if you have no box this will error.\nsyntax: `" + PREFIX + "remove [ping or item]`", false)
                        .addField("ping", "checks the bot's ping is ms.\nsyntax: `" + PREFIX + "ping`", false)
                        .addField("open", "opens a new box for you, with items if specified.\nsyntax:\n`" + PREFIX + "open`,\n`" + PREFIX + "open [ping or item]`", false)
                        .addField("delete", "deletes your box from our files permanently.\nsyntax: `" + PREFIX + "delete`", false)
                        .addField("list", "shows you all users and items in your or another user's box.\nsyntax:\n`" + PREFIX + "list`,\n`" + PREFIX + "list [ping]`", false)
                        .addField("commands", "lists ioxbox's available commands.\nsyntax: `" + PREFIX + "commands`", false)
                        .addField("help", "general information about ioxbox.\nsyntax: `" + PREFIX + "help`", false);
                channel.sendMessage(commandEmbed.build()).queue();
                FRAME.log(LogType.CMD, "commands", author);
                break;

            case "add":

                //if there are no mentioned users, use the first argument
                if (eventMessage.getMentionedUsers().isEmpty() && messageContent.length > 1) {
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
                //if we have a mention use it
                } else if (eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
                    if (author.hasBox()) {
                        HandleAdd yes = new HandleAdd(user, author, channel);
                        ConfirmationGetter.EXECUTOR.submit(yes);
                        break;
                    } else {
                        channel.sendMessage(helper.errorEmbed("you have no box to add to:\nwhy not open with " + PREFIX + "open?")).queue();
                    }
                } else {
                    channel.sendMessage(helper.errorEmbed("error adding to box: nothing found to add in message")).queue();
                }
                FRAME.log(LogType.CMD, PREFIX + "add", author);
                break;

            case "remove":
                //if there are no mentioned users and we have an item to remove, use the first argument
                if (eventMessage.getMentionedUsers().isEmpty() && messageContent.length > 1) {
                    if (author.hasBox() && author.getBox().contains(messageContent[1])) {
                        author.getBox().remove(messageContent[1]);
                        channel.sendMessage(helper.successEmbed(
                                "successfully removed item from box!",
                                "items:\n" + author.getBox().itemsToString()
                        )).queue();
                    } else {
                        channel.sendMessage(helper.errorEmbed("error removing from box: box does not exist or does not contain the requested item")).queue();
                    }
                } else if (eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
                    if (author.hasBox() && author.getBox().contains(user)) {
                        author.getBox().remove(user);
                        channel.sendMessage(helper.successEmbed(
                                "successfully removed user from box!",
                                "users:\n" + author.getBox().usersToString()
                        )).queue();
                    } else {
                        channel.sendMessage(helper.errorEmbed("error removing from box: user's box does not contain the requested user or does not exist")).queue();
                    }
                } else {
                    channel.sendMessage(helper.errorEmbed("error removing from box: nothing found to remove in message")).queue();
                }
                FRAME.log(LogType.CMD, PREFIX + "remove", author);
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
                FRAME.log(LogType.CMD, PREFIX + "open", author);
                break;

            case "delete":

                if (author.hasBox()) {
                    final HandleDelete yes = new HandleDelete(author, channel);
                    ConfirmationGetter.EXECUTOR.submit(yes);
                    break;
                } else {
                    channel.sendMessage(helper.errorEmbed("no box found to remove")).queue();
                }

                FRAME.log(LogType.CMD, PREFIX + "add", author);
                break;

            case "list":
                if (event.getMessage().getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(event.getMessage().getMentionedUsers().stream().findFirst().get());
                    if (user.hasBox()) {
                        event.getChannel().sendMessage(user.getBox().embed()).queue();
                    } else {
                        event.getChannel().sendMessage(helper.errorEmbed("this user doesn't seem to have a box. they can try opening a new one with " + PREFIX + "open!")).queue();
                    }
                } else {
                    if (author.hasBox()) {
                        event.getChannel().sendMessage(author.getBox().embed()).queue();
                    } else {
                        event.getChannel().sendMessage(helper.errorEmbed("you don't seem to have a box. try opening a new one with " + PREFIX + "open!")).queue();
                    }
                }
                FRAME.log(LogType.CMD, PREFIX + "add", author);
                break;

            case "ping":
                long time = System.currentTimeMillis();
                channel.sendMessage(helper.successEmbed("calculating ping...")).queue(message ->
                        message.editMessage(helper.successEmbed("ioxbox's ping is: " + (System.currentTimeMillis() - time) + "ms")).queue());
                FRAME.log(LogType.CMD, "ping", author);
                break;
            case "pickup":
                event.getChannel().sendMessage(PICKUPS[Main.RANDOM.nextInt(PICKUPS.length)]).queue();
                break;
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
            } else {
                Box.createBox(owner, content);
            }

            channel.sendMessage(helper.successEmbed("box successfully created with item " + content + "!")).queue();
        } catch (InvalidParameterException e) {
            channel.sendMessage(helper.errorEmbed("you seem to already have a box.")).queue();
        } catch (IllegalArgumentException e) {
            channel.sendMessage(helper.errorEmbed(e + ": the object passed to Box#createBox(Object, Object) was of an incompatible type")).queue();
        }
    }
}
