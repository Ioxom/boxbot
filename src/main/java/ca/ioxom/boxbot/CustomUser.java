package ca.ioxom.boxbot;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv8tion.jda.api.entities.User;

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

    //jackson need a parameterless constructor so it can properly deserialize JSON
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
}
