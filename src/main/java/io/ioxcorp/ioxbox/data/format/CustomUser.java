package io.ioxcorp.ioxbox.data.format;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ioxcorp.ioxbox.Main;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

//the JDA User class is too convoluted to save to JSON, so we only keep what we need
public class CustomUser {
    @JsonProperty("id")
    public final long id;
    @JsonProperty("username")
    public final String username;
    @JsonProperty("tag")
    public final int tag;
    public CustomUser(User user) {
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

    public CustomUser(long id, String username, int tag) {
        this.id = id;
        this.username = username;
        this.tag = tag;
    }

    public String toString() {
        return "id: " + this.id + "\ntag: " + this.username + "#" + this.tag;
    }

    @JsonIgnore
    public String getTag() {
        return this.username + "#" + this.tag;
    }

    public boolean hasBox() {
        return Main.boxes.containsKey(this.id);
    }

    @JsonIgnore
    public Box getBox() {
        return Main.boxes.get(this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUser that = (CustomUser) o;
        return id == that.id && tag == that.tag && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, tag);
    }
}
