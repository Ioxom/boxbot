package ca.ioxom.boxbot;

import net.dv8tion.jda.api.entities.User;

public class CustomUser {
    private final long id;
    private final String tag;
    public CustomUser(User user) {
        this.id = user.getIdLong();
        this.tag = user.getAsTag();
    }

    public CustomUser(long id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public long getId() {
        return this.id;
    }
}
