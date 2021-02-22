package io.ioxcorp.ioxbox.data.format;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ioxcorp.ioxbox.Main;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

//the JDA User class is too convoluted to save to JSON, so we only keep what we need
public final class CustomUser {
    private final long id;
    private final String username;
    private final int tag;

    /**
     * creates a {@link CustomUser CustomUser} from the {@link User User} object passed, essentially stripping down the data to its most important fields
     * @param user the {@link User User} object to be converted
     * @author ioxom
     */
    public CustomUser(final User user) {
        this.id = user.getIdLong();
        String[] splitTag = user.getAsTag().split("#");
        this.tag = Integer.parseInt(splitTag[1]);
        this.username = splitTag[0];
    }

    @JsonCreator
    public CustomUser() {
        this.id = 0L;
        this.username = "";
        this.tag = 0;
    }

    public String toString() {
        return "id: " + this.id + "\ntag: " + this.username + "#" + this.tag;
    }

    /**
     * @return the formatting most users see in discord of a {@link User User}: something like bob#1000
     * @author ioxom
     */
    @JsonIgnore
    public String getTag() {
        return this.username + "#" + this.tag;
    }

    /**
     * @return a string that if sent in discord will ping the user
     * @author ioxom
     */
    @JsonIgnore
    public String getPing() {
        return "<@" + this.id + ">";
    }

    /**
     * checks the {@link java.util.HashMap HashMap} contained in {@link Main Main} for a box owned by the user
     * @return true, if the {@link CustomUser CustomUser} has a box; false, if they do not
     * @author ioxom
     */
    public boolean hasBox() {
        return Main.BOXES.containsKey(this.id);
    }

    /**
     * get the box owned by the user from {@link Main Main's} {@link java.util.HashMap HashMap} of BOXES
     * @return the found {@link Box Box}, null if there is no box owned by the user
     * @author ioxom
     */
    @JsonIgnore
    public Box getBox() {
        return Main.BOXES.get(this.id);
    }

    public long getId() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomUser that = (CustomUser) o;
        return id == that.id && tag == that.tag && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, tag);
    }
}
