package io.ioxcorp.ioxbox.listeners;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.data.json.JacksonYeehawHelper;
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

    private static final String[][] COMMANDS = {
            {"help", "h"},
            {"commands", "cmds", "c" },
            {"add", "a"},
            {"remove", "r"},
            {"open", "o"},
            {"delete", "del", "d"},
            {"list", "l"},
            {"ping", "p"},
            {"config", "cfg"}
    };

    private static String getCommandName(final int index) {
        //index 0 is the "main" name of the command
        return Main.getConfig().getPrefix() + COMMANDS[index][0];
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {

        final String prefix = Main.getConfig().getPrefix();
        final Message eventMessage = event.getMessage();
        final String messageContentRaw = eventMessage.getContentRaw().toLowerCase();
        final MessageChannel channel = eventMessage.getChannel();

        if (messageContentRaw.startsWith(prefix) && !event.getAuthor().isBot()) {
            final String[] messageContent = messageContentRaw.split(prefix)[1].split(" ");
            final CustomUser author = new CustomUser(event.getAuthor());
            final EmbedHelper helper = new EmbedHelper(author);

            for (int i = 0; i < COMMANDS.length; i++) {

                //check if the array of command aliases contains the requested command
                boolean isAlias = false;
                for (int j = 0; j < COMMANDS[i].length; j++) {
                    if (messageContent[0].equals(COMMANDS[i][j])) {
                        isAlias = true;
                        break;
                    }
                }

                if (isAlias) {
                    //this is used to check what config value was changed for logging
                    String editedConfigValue = null;

                    switch (i) {
                        case 0:
                            EmbedBuilder helpEmbed = new EmbedBuilder()
                                    .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                                    .setColor(helper.getRandomEmbedColour())
                                    .addField("what in the heck does this bot do?", "this bot is very hot, it stores cool things for you. like words, words and more words for now.", false)
                                    .addField("commands", "use " + prefix + "commands for a list", false)
                                    .addField("ioxcorp™ inc", "ioxcorp™ inc. was founded in 04/01/20 by ioxom. it is also maintained by thonkman.", false)
                                    .setFooter("powered by ioxcorp™");
                            channel.sendMessage(helpEmbed.build()).queue();
                            break;

                        case 1:
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
                            break;

                        case 2:
                            if (/* check for pinged users */ eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
                                final CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
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
                            break;

                        case 3:
                            if (!author.hasBox()) {
                                channel.sendMessage(helper.errorEmbed("error removing from box: this box does not exist")).queue();
                                break;
                            }

                            if (/* if we have a mentioned user */ eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
                                final CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
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
                            break;

                        case 4:
                            if (eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
                                final CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
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
                            break;

                        case 5:
                            if (author.hasBox()) {
                                final HandleDelete yes = new HandleDelete(author, channel);
                                ConfirmationGetter.EXECUTOR.submit(yes);
                                break;
                            } else {
                                channel.sendMessage(helper.errorEmbed("no box found to remove")).queue();
                            }
                            break;

                        case 6:
                            if (/* check for pinged users */ eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
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
                            break;

                        case 7:
                            long time = System.currentTimeMillis();
                            channel.sendMessage(helper.successEmbed("calculating ping...")).queue(message ->
                                    message.editMessage(helper.successEmbed("ioxbox's ping is: " + (System.currentTimeMillis() - time) + "ms")).queue());
                            break;

                        case 8:
                            if (getConfig().getAdmins().contains(author.getId())) {
                                if (messageContent.length > 2) {
                                    switch (messageContent[1]) {
                                        case "admin":
                                        case "admins":
                                            if (messageContent[2].equals("add") && messageContent.length > 3) {
                                                try {
                                                    getConfig().getAdmins().add(Long.parseLong(messageContent[3]));
                                                    JacksonYeehawHelper.saveConfig();
                                                    channel.sendMessage(helper.successEmbed("added admin " + MessageHelper.getAsPing(messageContent[3]))).queue();
                                                    editedConfigValue = "added admin " + messageContent[3];
                                                } catch (NumberFormatException e) {
                                                    channel.sendMessage(helper.errorEmbed("failed to add admin: id is invalid")).queue();
                                                }
                                            } else if (messageContent[2].equals("remove") && messageContent.length > 3) {
                                                try {
                                                    getConfig().getAdmins().remove(Long.parseLong(messageContent[3]));
                                                    JacksonYeehawHelper.saveConfig();
                                                    channel.sendMessage(helper.successEmbed("removed admin " + MessageHelper.getAsPing(messageContent[3]))).queue();
                                                    editedConfigValue = "removed admin " + messageContent[3];
                                                } catch (NumberFormatException e) {
                                                    channel.sendMessage(helper.errorEmbed("failed to remove admin: id is invalid")).queue();
                                                }
                                            } else {
                                                channel.sendMessage(helper.errorEmbed("not enough arguments or unknown command")).queue();
                                            }
                                            break;
                                        case "prefix":
                                            if (messageContent[2].equals("set") && messageContent.length > 3) {
                                                final String newPrefix = messageContent[3] + (messageContent.length > 4 && messageContent[4].equals("-addspace") ? " " : "");
                                                System.out.println(messageContent[3]);
                                                System.out.println(newPrefix);
                                                getConfig().setPrefix(newPrefix);
                                                channel.sendMessage(helper.successEmbed("set prefix to [" + newPrefix + "]")).queue();
                                                editedConfigValue = "set prefix to [" + newPrefix + "]";
                                            } else {
                                                channel.sendMessage(helper.errorEmbed("not enough arguments or unknown command")).queue();
                                            }
                                            break;
                                        case "logcommands":
                                            final boolean log = Boolean.parseBoolean(messageContent[2]);
                                            getConfig().setCommandLogging(log);
                                            channel.sendMessage(helper.successEmbed("set logCommands to " + log)).queue();
                                            editedConfigValue = "set logCommands to " + log;
                                            break;
                                    }
                                } else {
                                    channel.sendMessage(helper.errorEmbed("not enough arguments")).queue();
                                }
                            } else {
                                channel.sendMessage(helper.errorEmbed("you do not have sufficient permissions to edit configuration")).queue();
                            }
                    }

                    //log what command was used
                    FRAME.log(LogType.CMD, getCommandName(i), author);
                    //log config
                    if (i == 8) {
                        FRAME.log(LogType.MAIN, "edited config value: " + editedConfigValue);
                        FRAME.log(LogType.MAIN, "new config: \n" + Main.getConfig());
                    }
                }
            }
        } else if (messageContentRaw.contains("++")) {
            Main.h.handle(messageContentRaw, channel);
        }
    }

    private void createBox(final CustomUser owner, final String content, final EmbedHelper helper, final MessageChannel channel) {
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
