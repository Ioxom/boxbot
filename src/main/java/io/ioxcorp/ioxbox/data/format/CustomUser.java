package io.ioxcorp.ioxbox.data.format;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.old.OldCustomUser;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

/**
 * this class is used when saving to json instead of {@link User} because that errors on save
 * @author ioxom
 */
public final class CustomUser {
    @JsonProperty("id")
    private final long id;
    @JsonProperty("username")
    private final String username;
    @JsonProperty("discriminator")
    private final int discriminator;

    /**
     * creates a {@link CustomUser CustomUser} from the {@link User User} object passed, essentially stripping down the data to its most important fields
     * @param user the {@link User User} object to be converted
     */
    public CustomUser(final User user) {
        this.id = user.getIdLong();
        String[] splitTag = user.getAsTag().split("#");
        this.discriminator = Integer.parseInt(splitTag[1]);
        this.username = splitTag[0];
    }

    public CustomUser(final OldCustomUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.discriminator = user.getDiscriminator();
    }

    @JsonCreator
    public CustomUser() {
        this.id = 0L;
        this.username = "";
        this.discriminator = 0;
    }

    public String toString() {
        return "id: " + this.id + "\ntag: " + this.username + "#" + this.discriminator;
    }

    /**
     * @return the formatting most users see in discord of a {@link User User}: something like bob#1000
     */
    @JsonIgnore
    public String getAsTag() {
        return this.username + "#" + this.discriminator;
    }

    /**
     * @return a string that if sent in discord will ping the user
     */
    @JsonIgnore
    public String getPing() {
        return "<@" + this.id + ">";
    }

    /**
     * checks the {@link java.util.HashMap HashMap} contained in {@link Main Main} for a box owned by the user
     * @return true, if the {@link CustomUser CustomUser} has a box; false, if they do not
     */
    public boolean hasBox() {
        return Main.BOXES.containsKey(this.id);
    }

    /**
     * get the box owned by the user from {@link Main Main's} {@link java.util.HashMap HashMap} of boxes
     * @return the found {@link Box Box}, null if there is no box owned by the user
     */
    @JsonIgnore
    public Box getBox() {
        return Main.BOXES.get(this.id);
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CustomUser that = (CustomUser) o;
        return id == that.id && discriminator == that.discriminator && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, discriminator);
    }

    @JsonGetter
    public String getUsername() {
        return username;
    }

    @JsonGetter
    public long getId() {
        return this.id;
    }

    @JsonGetter
    public int getDiscriminator() {
        return discriminator;
    }
}
