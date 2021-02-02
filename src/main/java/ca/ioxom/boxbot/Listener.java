package ca.ioxom.boxbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class Listener extends ListenerAdapter {
    private boolean isCheckingForResponse;
    private User checkedUser;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        switch (event.getMessage().getContentRaw().toLowerCase()) {
            case "-box add" :
                event.getChannel().sendMessage("proof that im getting the command").queue();
                /*try {
                    String addToBox = event.getMessage().getContentRaw().toLowerCase();
                    FileWriter myWriter = new FileWriter("C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\ca\\ioxom\\boxbot\\Files\\boxmoment.txt");
                    myWriter.write("\n" + Arrays.toString(addToBox.split("-box add")));
                    myWriter.close();
                    event.getChannel().sendMessage("Succesfully added item" + addToBox + "to box").queue();
                    System.out.println("i tried \n\n\n\n\n\n\n\n\nagain");



                } catch (IOException e) {
                    System.out.println("did something wrong i think\n\n\n\n\n\nnotice me senpai");
                    e.printStackTrace();
                }*/
                break;
            case "-box yes" :
                event.getChannel().sendMessage("Box is here :package: ").queue();
                break;
            case "-box help" :
                //this is the format I want
                EmbedBuilder helpEmbed = new EmbedBuilder()
                        .setAuthor("The Box People :tm:")
                        .setColor(new Color(0xfc03df))
                        .addField("What in the heck does this bot do?", "This bot is very hot, it stores cool things for you. Like words, words and more words for now.", false)
                        .addField("Commands", "Prefix is -box \n Commands are the following; yes, add", false)
                        .addField("The IoxCorp Incorporated", "IoxCorp Incorporated was founded in 04/01/20 by the \"Ioxom Foundation\", and is actively being sponsored by \" The Birch Tree\", It is also funded by \"Thonkman\"\nIf you would like to learn more, please use the command -ioxcorp (nonexistant atm)", false)
                        .setFooter("Powered by electricity *and ioxcorp:tm:*");
                event.getChannel().sendMessage(helpEmbed.build()).queue();
                break;
        }
    }
}
