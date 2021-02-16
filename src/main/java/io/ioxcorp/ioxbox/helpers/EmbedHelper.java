package io.ioxcorp.ioxbox.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Random;

public class EmbedHelper {
    private final CustomUser user;
    private final Random random;
    private final String[] author = {
            "ioxbox",
            "https://ioxom.github.io/ioxbox/",
            "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png"
    };

    public EmbedHelper(CustomUser user) {
        this.user = user;
        this.random = new Random();
    }

    @JsonIgnore
    private String getBoxID() {
        if (user.hasBox()) {
            return "" + user.id;
        } else {
            return "null - no box";
        }
    }

    @JsonIgnore
    public static String getBoxID(CustomUser user) {
        if (user.hasBox()) {
            return "" + user.id;
        } else {
            return "null - no box";
        }
    }

    public MessageEmbed errorEmbed(String error) {

        //5% chance for rotater
        boolean rotater = random.nextInt(100 + 1) - 1 > 95;

        return new EmbedBuilder()
                .setColor(0xC91A00)
                .setAuthor(author[0], author[1], author[2])
                .setDescription(error + (rotater? "have a rotater!" : ""))
                .setThumbnail(rotater? "https://raw.githubusercontent.com/ioxom/ioxbox/master/src/main/resources/gifs/rotater.gif" : null)
                .setFooter("requested by user " + user.getTag() + "\nbox id: " + getBoxID())
                .build();
    }

    public MessageEmbed successEmbed(String message) {
        return new EmbedBuilder()
                .setAuthor(author[0], author[1], author[2])
                .setColor(0x00FF00)
                .setDescription(message)
                .setFooter("requested by user " + user.getTag() + "\nbox id: " + getBoxID())
                .build();
    }

    public MessageEmbed successEmbed(String title, String message) {
        return new EmbedBuilder()
                .setAuthor(author[0], author[1], author[2])
                .setColor(0x00FF00)
                .setDescription(message)
                .setTitle(title)
                .setFooter("requested by user " + user.getTag() + "\nbox id: " + getBoxID())
                .build();
    }

    public static MessageEmbed simpleErrorEmbed(long id, String message) {
        return new EmbedBuilder()
                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                .setColor(0xC91A00)
                .setDescription(message)
                .setFooter("requested by user with id: " + id)
                .build();
    }
}