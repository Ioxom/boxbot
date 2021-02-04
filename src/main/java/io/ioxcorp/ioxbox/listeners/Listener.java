package io.ioxcorp.ioxbox.listeners;

import static io.ioxcorp.ioxbox.Main.boxes;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import net.dv8tion.jda.api.EmbedBuilder;
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

        if (!messageContentRaw.startsWith(prefix)) return;
        String[] message = messageContentRaw.split(prefix)[1].split(" ");
        CustomUser author = new CustomUser(event.getAuthor());

        switch (message[0]) {
            //TODO: 0.2.0: remove this
            case "yes":
                event.getChannel().sendMessage("Box is here :package: ").queue();
                break;
            case "help":
                EmbedBuilder helpEmbed = new EmbedBuilder()
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(new Color(0xfc03df))
                        .addField("what in the heck does this bot do?", "this bot is very hot, it stores cool things for you. like words, words and more words for now.", false)
                        .addField("commands", "prefix is -box \n commands are the following; yes, add", false)
                        .addField("ioxcorp™ inc", "ioxcorp™ inc. was founded in 04/01/20 by ioxom. it is also maintained by thonkman.", false)
                        .setFooter("powered by ioxcorp™");
                event.getChannel().sendMessage(helpEmbed.build()).queue();
                break;
            //TODO: 0.2.0: instead of listing contents list only the affected variables and give success or failure dialogue
            case "add":
                //if there are no mentioned users, use the first argument
                if (event.getMessage().getMentionedUsers().isEmpty() && message.length > 1) {
                    if (boxes.containsKey(author.id)) {
                        author.getBox().add(message[1]);
                    } else {
                        try {
                            Box.createBox(author, message[1]);

                            //this exception is never thrown because this code can only be executed if the user does not have a box
                        } catch (IOException ignored) {}
                    }
                } else if (event.getMessage().getMentionedUsers().stream().findFirst().isPresent()) {
                    //TODO: 0.3.0: require confirmation from the user being boxed
                    CustomUser user = new CustomUser(event.getMessage().getMentionedUsers().stream().findFirst().get());
                    if (boxes.containsKey(author.id)) {
                        author.getBox().add(user);
                    } else {
                        try {
                            Box.createBox(author, user);

                            //this exception is never thrown because this code can only be executed if the user does not have a box
                        } catch (IOException ignored) {}
                    }
                } else {
                    event.getChannel().sendMessage(new EmbedBuilder()
                            .setColor(0x00FF00)
                            .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                            .setDescription("error adding to box: nothing found to add in message")
                            .build()
                    ).queue();
                }
                event.getChannel().sendMessage(boxes.get(author.id).embed()).queue();
                Main.frame.logCommand(author, "box add", true);
                break;
            case "remove":
                //if there are no mentioned users, use the first argument
                if (event.getMessage().getMentionedUsers().isEmpty() && message.length > 1) {
                    if (boxes.containsKey(author.id)) {
                        if (author.getBox().contains(message[1])) author.getBox().remove(message[1]);
                    } else {
                        event.getChannel().sendMessage(new EmbedBuilder()
                                .setColor(0x00FF00)
                                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                                .setDescription("error removing from box: box does not exist")
                                .build()
                        ).queue();
                    }
                } else if (event.getMessage().getMentionedUsers().stream().findFirst().isPresent()) {
                    CustomUser user = new CustomUser(event.getMessage().getMentionedUsers().stream().findFirst().get());
                    if (boxes.containsKey(author.id)) {
                        if (author.getBox().contains(user)) author.getBox().remove(user);
                    } else {
                        event.getChannel().sendMessage(new EmbedBuilder()
                                .setColor(0x00FF00)
                                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                                .setDescription("error removing from box: box does not exist")
                                .build()
                        ).queue();
                    }
                } else {
                    event.getChannel().sendMessage(new EmbedBuilder()
                            .setColor(0x00FF00)
                            .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                            .setDescription("error removing from box: nothing found to remove in message")
                            .build()
                    ).queue();
                }
                event.getChannel().sendMessage(boxes.get(author.id).embed()).queue();
                Main.frame.logCommand(author, "box remove", true);
                break;
            case "open":
                if (message.length == 1) {
                    try {
                        Box.createBox(author);
                        event.getChannel().sendMessage(new EmbedBuilder()
                                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                                .setColor(0x00FF00)
                                .setDescription("empty box successfully created!")
                                .build()
                        ).queue();
                    } catch (IOException e) {
                        event.getChannel().sendMessage(new EmbedBuilder()
                                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                                .setColor(0x00FF00)
                                .setDescription("you seem to already have a box. here have a rotater instead.")
                                .setThumbnail("https://media.discordapp.net/attachments/722951540972978188/806690297894797322/rotater.gif")
                                .build()
                        ).queue();
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
                        event.getChannel().sendMessage(new EmbedBuilder()
                                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                                .setColor(0x00FF00)
                                .setDescription("you seem to already have a box. here have a rotater instead.")
                                .setThumbnail("https://media.discordapp.net/attachments/722951540972978188/806690297894797322/rotater.gif")
                                .build()
                        ).queue();
                    }
                }
                break;

            //TODO: 0.3.0: require confirmation
            case "delete":
                boxes.remove(author.id);
                event.getChannel().sendMessage(new EmbedBuilder()
                        .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                        .setColor(0x00FF00)
                        .setDescription("your box was successfully deleted!")
                        .build()
                ).queue();
                break;

            //TODO: 0.2.0: "list" [user id or ping] (uses author if not present) command to list box contents
            //TODO: 0.2.0: alnex this is not what I envisioned
            case "content":
                try {
                    if (author.hasBox()) {
                        try {
                            event.getChannel().sendMessage("YoUwU have a box!\n").queue();
                            for (String item : author.getBox().items) {
                                event.getChannel().sendMessage(item).queue();
                            }
                        } catch (IndexOutOfBoundsException e) {
                            event.getChannel().sendMessage("OwO, whats this? You don't appear to have something in that slot.").queue();
                        }
                    } else {
                        event.getChannel().sendMessage("You don't have a box you UwUdiot.").queue();
                    }
                } catch (Exception e) {
                    event.getChannel().sendMessage("There was an error TwT").queue();
                    e.printStackTrace();
                }


                Main.frame.logCommand(author, "box context", true);

                break;
        }
    }
}