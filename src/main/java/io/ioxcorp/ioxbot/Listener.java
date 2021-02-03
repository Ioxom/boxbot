package io.ioxcorp.ioxbot;

import static io.ioxcorp.ioxbot.Main.boxes;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

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
                //TODO: 0.2.0: this is bad
                EmbedBuilder helpEmbed = new EmbedBuilder()
                        .setAuthor("The Box People :tm:")
                        .setColor(new Color(0xfc03df))
                        .addField("What in the heck does this bot do?", "This bot is very hot, it stores cool things for you. Like words, words and more words for now.", false)
                        .addField("Commands", "Prefix is -box \n Commands are the following; yes, add", false)
                        .addField("The IoxCorp Incorporated", "IoxCorp Incorporated was founded in 04/01/20 by the \"Ioxom Foundation\", and is actively being sponsored by \" The Birch Tree\", It is also funded by \"Thonkman\"\nIf you would like to learn more, please use the command -ioxcorp (nonexistent atm)", false)
                        .setFooter("Powered by electricity *and ioxcorp:tm:*");
                event.getChannel().sendMessage(helpEmbed.build()).queue();
                break;
            //TODO: 0.2.0: allow for parsing of user objects
            case "add":
                if (boxes.containsKey(author.id)) {
                    Box.addToBoxOfUser(author, message[1]);
                } else {
                    Box.createBox(author, message[1]);
                }
                event.getChannel().sendMessage(boxes.get(author.id).embed()).queue();
                JacksonYeehawHelper.save(boxes);
                Main.frame.logCommand(author, "box add", true);
                break;
            case "remove":
                if (boxes.containsKey(author.id)) {
                    if (author.getBox().contains(message[1])) Box.removeFromBoxOfUser(author, message[1]);
                } else {
                    event.getChannel().sendMessage(new EmbedBuilder()
                            .setColor(0x00FF00)
                            .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                            .setDescription("error removing from box: box does not exist").build()
                    ).queue();
                }
                event.getChannel().sendMessage(boxes.get(author.id).embed()).queue();
                JacksonYeehawHelper.save(boxes);
                Main.frame.logCommand(author, "box remove", true);
                break;
        }
    }
}