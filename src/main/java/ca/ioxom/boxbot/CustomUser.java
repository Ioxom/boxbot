package ca.ioxom.boxbot;

import net.dv8tion.jda.api.entities.User;

public class CustomUser {
    private final long id;
    private final String username;
    private final int tag;
    public CustomUser(User user) {
        this.id = user.getIdLong();
        String[] splitTag = user.getAsTag().split("#");
        this.tag = Integer.parseInt(splitTag[1]);
        this.username = splitTag[0];
    }

    public CustomUser(long id, String username, int tag) {
        this.id = id;
        this.username = username;
        this.tag = tag;
    }

    public String getAsTag() {
        return this.username + this.tag;
    }

    public String getUsername() {
        return this.username;
    }

    public long getId() {
        return this.id;
    }

    public int getTag() {
        return this.tag;
    }
}
