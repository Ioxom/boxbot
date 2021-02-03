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
            //TODO: 0.2.0: allow for parsing of user objects
            //TODO: 0.2.0: instead of listing contents list only the affected variables and give success or failure dialogue
            case "add":
                if (boxes.containsKey(author.id)) {
                    author.getBox().add(message[1]);
                } else {
                    try {
                        Box.createBox(author, message[1]);

                    //this exception is never thrown because this code can only be executed if the user does not have a box
                    } catch (IOException ignored) {}
                }
                event.getChannel().sendMessage(boxes.get(author.id).embed()).queue();
                Main.frame.logCommand(author, "box add", true);
                break;
            case "remove":
                if (boxes.containsKey(author.id)) {
                    if (author.getBox().contains(message[1])) author.getBox().remove(message[1]);
                } else {
                    event.getChannel().sendMessage(new EmbedBuilder()
                            .setColor(0x00FF00)
                            .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                            .setDescription("error removing from box: box does not exist").build()
                    ).queue();
                }
                event.getChannel().sendMessage(boxes.get(author.id).embed()).queue();
                Main.frame.logCommand(author, "box remove", true);
                break;
            case "open":
                try {
                    Box.createBox(author, message[1]);
                } catch (IOException e) {
                    event.getChannel().sendMessage(new EmbedBuilder()
                            .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                            .setColor(0x00FF00)
                            .setDescription("error: user already has box")
                            .build()
                    ).queue();
                }

                //TODO: 0.2.0: "list" [user id or ping] (uses author if not present) command to list box contents
            case "content":
                try {
                    event.getChannel().sendMessage("got the command").queue();
                    if (author.hasBox()) {
                        event.getChannel().sendMessage("You have a box!").queue();
                        event.getChannel().sendMessage(author.getBox().items).queue();


                    } else {
                        event.getChannel().sendMessage("You dont have a box you idiot");
                    }
                } catch (Exception e) {
                    event.getChannel().sendMessage("Yes");
                    e.printStackTrace();
                }


                Main.frame.logCommand(author, "box context", true);

                break;
        }
    }
}