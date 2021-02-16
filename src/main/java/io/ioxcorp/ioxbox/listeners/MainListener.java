package io.ioxcorp.ioxbox.listeners;

import static io.ioxcorp.ioxbox.Main.boxes;
import static io.ioxcorp.ioxbox.Main.frame;
import static io.ioxcorp.ioxbox.Frame.LogType;

import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.listeners.confirmation.handlers.HandleAdd;
import io.ioxcorp.ioxbox.listeners.confirmation.handlers.HandleDelete;
import io.ioxcorp.ioxbox.listeners.confirmation.handlers.HandleOpenWithUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.security.InvalidParameterException;
import java.util.Random;

public class MainListener extends ListenerAdapter {
    public static final Random random = new Random();
    
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {


        final String prefix = "-box ";
        final Message eventMessage = event.getMessage();
        final String messageContentRaw = eventMessage.getContentRaw().toLowerCase();

        if (!messageContentRaw.startsWith(prefix) || event.getAuthor().isBot()) return;

        final String[] messageContent = messageContentRaw.split(prefix)[1].split(" ");
        final CustomUser author = new CustomUser(event.getAuthor());
        final EmbedHelper helper = new EmbedHelper(author);
        final MessageChannel channel = eventMessage.getChannel();

        switch (messageContent[0]) {
            //TODO: 0.4.0: make this not a mess
            case "help":
                EmbedBuilder helpEmbed = new EmbedBuilder()
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(new Color(0xfc03df))
                        .addField("what in the heck does this bot do?", "this bot is very hot, it stores cool things for you. like words, words and more words for now.", false)
                        .addField("commands", "prefix is " + prefix + "\n commands are the following; yes, add", false)
                        .addField("ioxcorp™ inc", "ioxcorp™ inc. was founded in 04/01/20 by ioxom. it is also maintained by thonkman.", false)
                        .setFooter("powered by ioxcorp™");
                channel.sendMessage(helpEmbed.build()).queue();
                frame.log(LogType.CMD, "help", author);
                break;
            case "add":
                //if there are no mentioned users, use the first argument
                if (eventMessage.getMentionedUsers().isEmpty()) {
                    if (boxes.containsKey(author.id)) {
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
                    if (boxes.containsKey(author.id)) {
                        channel.sendMessage(helper.successEmbed(user + " would you like to join " + author.getTag() + "'s box? (Type yes to accept)")).queue();
                        HandleAdd yes = new HandleAdd(user, author, channel);
                        new Thread(yes).start();
                        break;
                    } else {
                        channel.sendMessage(helper.errorEmbed("you have no box to add to:\nwhy not open with -box open?")).queue();
                    }
                } else {
                    channel.sendMessage(helper.errorEmbed("error adding to box: nothing found to add in message")).queue();
                }
                frame.log(LogType.CMD, prefix + "add", author);
                break;
            case "remove":
                //if there are no mentioned users, use the first argument
                if (eventMessage.getMentionedUsers().isEmpty() && messageContent.length > 1) {
                    if (boxes.containsKey(author.id)) {
                        if (author.getBox().contains(messageContent[1])) {
                            author.getBox().remove(messageContent[1]);
                            channel.sendMessage(helper.successEmbed(
                                    "successfully removed item from box!",
                                    "items:\n" + author.getBox().itemsToString()
                            )).queue();
                        } else {
                            channel.sendMessage(helper.errorEmbed("error removing from box: box does not contain item")).queue();
                        }
                    } else {
                        channel.sendMessage(helper.errorEmbed("error removing from box: box does not exist")).queue();
                    }
                } else if (eventMessage.getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
                    if (boxes.containsKey(author.id)) {
                        if (author.getBox().contains(user)) author.getBox().remove(user);
                        channel.sendMessage(helper.successEmbed(
                                "successfully removed user from box!",
                                "users:\n" + author.getBox().usersToString()
                        )).queue();
                    } else {
                        channel.sendMessage(helper.errorEmbed("error removing from box: nothing found to remove in message")).queue();
                    }
                } else {
                    channel.sendMessage(helper.errorEmbed("error removing from box: nothing found to remove in message")).queue();
                }
                frame.log(LogType.CMD, prefix + "remove", author);
                break;

            case "open":
                if (!eventMessage.getMentionedUsers().isEmpty()) {
                    CustomUser user = new CustomUser(eventMessage.getMentionedUsers().stream().findFirst().get());
                    channel.sendMessage(helper.successEmbed("<!" + user.id + ">, do you want to be added to <!" + author.id + ">'s new box?")).queue();
                    HandleOpenWithUser handleOpenWithUser = new HandleOpenWithUser(user, author, channel);
                    new Thread(handleOpenWithUser).start();
                    break;
                } else {
                    try {
                        Box.createBox(author, messageContent[1]);
                        channel.sendMessage(helper.successEmbed("box successfully created with item " + messageContent[1] + "!")).queue();
                    } catch (InvalidParameterException e) {
                        channel.sendMessage(helper.errorEmbed("you seem to already have a box. here have a rotater instead!")).queue();
                    } catch (IllegalArgumentException e) {
                        channel.sendMessage(helper.errorEmbed(e + ": the object passed to Box#createBox(Object, Object) was of an incompatible type")).queue();
                    }
                }
                frame.log(LogType.CMD, prefix + "open", author);
                break;

            case "delete":
                if (boxes.containsKey(author.id)) {
                    channel.sendMessage(helper.successEmbed("delete box? this action is permanent and will remove everything in your box")).queue();
                    //TODO: 0.5.0: look into ThreadPool or other solutions instead of creating a new Thread every time
                    HandleDelete yes = new HandleDelete(author, channel);
                    new Thread(yes).start();
                    break;
                } else {
                    channel.sendMessage(helper.errorEmbed("no box found to remove")).queue();
                }
                frame.log(LogType.CMD, prefix + "add", author);
                break;

            case "list":
                if (event.getMessage().getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(event.getMessage().getMentionedUsers().stream().findFirst().get());
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
                frame.log(LogType.CMD, prefix + "add", author);
                break;

            case "ping":
                long time = System.currentTimeMillis();
                channel.sendMessage(helper.successEmbed("calculating ping...")).queue(message ->
                        message.editMessage(helper.successEmbed("ioxbox's ping is: " + (System.currentTimeMillis() - time) + "ms")).queue());
                frame.log(LogType.CMD, "ping", author);
                break;
            case "pickup":
                event.getChannel().sendMessage(pickups[random.nextInt(pickups.length)]).queue();
                break;
        }
    }

    private static final String[] pickups = {
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
}
