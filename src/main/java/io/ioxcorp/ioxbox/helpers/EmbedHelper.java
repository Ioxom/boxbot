package io.ioxcorp.ioxbox.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class EmbedHelper {
    public static final int SUCCESS_EMBED_COLOUR = 0x00FF00;
    public static final int ERROR_EMBED_COLOUR = 0xc91a00;

    private final CustomUser user;
    private final String[] author = {
            "ioxbox",
            "https://ioxom.github.io/ioxbox/",
            "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png"
    };

    public EmbedHelper(final CustomUser user) {
        this.user = user;
    }

    @JsonIgnore
    private String getBoxID() {
        if (user.hasBox()) {
            return "" + user.getId();
        } else {
            return "null - no box";
        }
    }

    @JsonIgnore
    public static String getBoxID(final CustomUser user) {
        if (user.hasBox()) {
            return "" + user.getId();
        } else {
            return "null - no box";
        }
    }

    public MessageEmbed errorEmbed(final String error) {

        //5% chance for rotater
        boolean rotater = Main.RANDOM.nextInt(100 + 1) - 1 > 95;

        return new EmbedBuilder()
                .setColor(ERROR_EMBED_COLOUR)
                .setAuthor(author[0], author[1], author[2])
                .setDescription(error + (rotater ? "have a rotater!" : ""))
                .setThumbnail(rotater ? "https://raw.githubusercontent.com/ioxom/ioxbox/master/src/main/resources/gifs/rotater.gif" : null)
                .setFooter("requested by user " + user.getTag() + "\nbox id: " + getBoxID())
                .build();
    }

    public MessageEmbed successEmbed(final String message) {
        return new EmbedBuilder()
                .setAuthor(author[0], author[1], author[2])
                .setColor(SUCCESS_EMBED_COLOUR)
                .setDescription(message)
                .setFooter("requested by user " + user.getTag() + "\nbox id: " + getBoxID())
                .build();
    }

    public MessageEmbed successEmbed(final String title, final String message) {
        return new EmbedBuilder()
                .setAuthor(author[0], author[1], author[2])
                .setColor(SUCCESS_EMBED_COLOUR)
                .setDescription(message)
                .setTitle(title)
                .setFooter("requested by user " + user.getTag() + "\nbox id: " + getBoxID())
                .build();
    }

    public static MessageEmbed simpleErrorEmbed(final long id, final String message) {
        return new EmbedBuilder()
                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                .setColor(ERROR_EMBED_COLOUR)
                .setDescription(message)
                .setFooter("requested by user with id: " + id)
                .build();
    }
}
