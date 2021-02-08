package io.ioxcorp.ioxbox.listeners;

import static io.ioxcorp.ioxbox.Main.boxes;

import io.ioxcorp.ioxbox.Frame;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.io.IOException;

//unfinished, being worked on by Thonkman
public class Listener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        final String prefix = "-box ";
        final String messageContentRaw = event.getMessage().getContentRaw().toLowerCase();

        if (!messageContentRaw.startsWith(prefix) || event.getAuthor().isBot()) return;
        String[] message = messageContentRaw.split(prefix)[1].split(" ");
        CustomUser author = new CustomUser(event.getAuthor());

        switch (message[0]) {
            //TODO: 0.4.0: make this not a mess
            case "help":
                EmbedBuilder helpEmbed = new EmbedBuilder()
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(new Color(0xfc03df))
                        .addField("what in the heck does this bot do?", "this bot is very hot, it stores cool things for you. like words, words and more words for now.", false)
                        .addField("commands", "prefix is " + prefix + "\n commands are the following; yes, add", false)
                        .addField("ioxcorp™ inc", "ioxcorp™ inc. was founded in 04/01/20 by ioxom. it is also maintained by thonkman.", false)
                        .setFooter("powered by ioxcorp™");
                event.getChannel().sendMessage(helpEmbed.build()).queue();
                break;
            case "add":
                //if there are no mentioned users, use the first argument
                if (event.getMessage().getMentionedUsers().isEmpty() && message.length > 1) {
                    if (boxes.containsKey(author.id)) {
                        author.getBox().add(message[1]);
                        event.getChannel().sendMessage(successEmbed(
                                "successfully added item to box!",
                                "items:\n" + author.getBox().itemsToString()
                        )).queue();
                    } else {
                        try {
                            Box.createBox(author, message[1]);
                            event.getChannel().sendMessage(successEmbed("box successfully created with item " + message[1] + "!")).queue();

                        //this exception is never thrown because this code can only be executed if the user does not have a box
                        } catch (IOException ignored) {}
                    }
                } else if (event.getMessage().getMentionedUsers().stream().findFirst().isPresent()) {
                    //TODO: 0.3.0: require confirmation from the user being boxed
                    CustomUser user = new CustomUser(event.getMessage().getMentionedUsers().stream().findFirst().get());
                    if (boxes.containsKey(author.id)) {
                        author.getBox().add(user);
                        event.getChannel().sendMessage(successEmbed(
                                "successfully added user to box!",
                                "users:\n" + author.getBox().usersToString()
                        )).queue();
                    } else {
                        try {
                            Box.createBox(author, user);
                            event.getChannel().sendMessage(successEmbed("box successfully created with user " + user.getTag() + "!")).queue();

                        //this exception is never thrown because this code can only be executed if the user does not have a box
                        } catch (IOException ignored) {}
                    }
                } else {
                    event.getChannel().sendMessage(errorEmbed("error adding to box: nothing found to add in message")).queue();
                }
                Main.frame.log(Frame.LogType.CMD, prefix + "add", author);
                break;
            case "remove":
                //if there are no mentioned users, use the first argument
                if (event.getMessage().getMentionedUsers().isEmpty() && message.length > 1) {
                    if (boxes.containsKey(author.id)) {
                        if (author.getBox().contains(message[1])) {
                            author.getBox().remove(message[1]);
                            event.getChannel().sendMessage(successEmbed(
                                    "successfully removed item from box!",
                                    "items:\n" + author.getBox().itemsToString()
                            )).queue();
                        } else {
                            event.getChannel().sendMessage(errorEmbed("error removing from box: box does not contain item")).queue();
                        }
                    } else {
                        event.getChannel().sendMessage(errorEmbed("error removing from box: box does not exist")).queue();
                    }
                } else if (event.getMessage().getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(event.getMessage().getMentionedUsers().stream().findFirst().get());
                    if (boxes.containsKey(author.id)) {
                        if (author.getBox().contains(user)) author.getBox().remove(user);
                        event.getChannel().sendMessage(successEmbed(
                                "successfully removed user from box!",
                                "users:\n" + author.getBox().usersToString()
                        )).queue();
                    } else {
                        event.getChannel().sendMessage(errorEmbed("error removing from box: nothing found to remove in message")).queue();
                    }
                } else {
                    event.getChannel().sendMessage(errorEmbed("error removing from box: nothing found to remove in message")).queue();
                }
                Main.frame.log(Frame.LogType.CMD, prefix + "remove", author);
                break;

            case "open":
                if (message.length == 1) {
                    try {
                        Box.createBox(author);
                        event.getChannel().sendMessage(successEmbed("empty box successfully created!")).queue();
                    } catch (IOException e) {
                        event.getChannel().sendMessage(errorEmbedWithRotater("you seem to already have a box. why not have a rotater instead of a new one!")).queue();
                    }
                } else {
                    try {
                        Box.createBox(author, message[1]);
                        event.getChannel().sendMessage(new EmbedBuilder()
                                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                                .setColor(0x00FF00)
                                .setDescription("box successfully created with item " + message[1] + "!")
                                .build()
                        ).queue();
                    } catch (IOException e) {
                        event.getChannel().sendMessage(errorEmbedWithRotater("you seem to already have a box. here have a rotater instead.")).queue();
                    }
                }
                Main.frame.log(Frame.LogType.CMD, prefix + "open", author);
                break;

            //TODO: 0.3.0: require confirmation
            case "delete":
                if (boxes.containsKey(author.id)) {
                    boxes.remove(author.id);
                    event.getChannel().sendMessage(successEmbed("your box was successfully deleted!")).queue();
                } else {
                    event.getChannel().sendMessage(errorEmbed("no box found to remove")).queue();
                }
                Main.frame.log(Frame.LogType.CMD, prefix + "add", author);
                break;

            case "list":
                //if we have a ping
                if (event.getMessage().getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(event.getMessage().getMentionedUsers().stream().findFirst().get());
                    if (user.hasBox()) {
                        event.getChannel().sendMessage(user.getBox().embed()).queue();
                    } else {
                        event.getChannel().sendMessage(errorEmbed("this user doesn't seem to have a box. they can try opening a new one with " + prefix + "open!")).queue();
                    }
                } else {
                    if (author.hasBox()) {
                        event.getChannel().sendMessage(author.getBox().embed()).queue();
                    } else {
                        event.getChannel().sendMessage(errorEmbed("you don't seem to have a box. try opening a new one with " + prefix + "open!")).queue();
                    }
                }
                Main.frame.log(Frame.LogType.CMD, prefix + "add", author);
                break;
        }
    }

    private MessageEmbed errorEmbed(String error) {
        return new EmbedBuilder()
                .setColor(0xC91A00)
                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                .setDescription(error)
                .build();
    }

    private MessageEmbed errorEmbedWithRotater(String error) {
        return new EmbedBuilder()
                .setColor(0xC91A00)
                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                .setDescription(error)
                .setThumbnail("https://media.discordapp.net/attachments/722951540972978188/806690297894797322/rotater.gif")
                .build();
    }

    private MessageEmbed successEmbed(String message) {
        return new EmbedBuilder()
                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                .setColor(0x00FF00)
                .setDescription(message)
                .build();
    }

    private MessageEmbed successEmbed(String title, String message) {
        return new EmbedBuilder()
                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                .setColor(0x00FF00)
                .setDescription(message)
                .setTitle(title)
                .build();
    }
}