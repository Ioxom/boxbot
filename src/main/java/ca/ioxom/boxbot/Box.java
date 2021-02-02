package ca.ioxom.boxbot;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class Box {
    @JsonProperty("owner")
    public CustomUser owner;
    @JsonProperty("items")
    public ArrayList<String> items;
    @JsonProperty("users")
    public ArrayList<CustomUser> users;
    public Box(Object owner, Object object) {
        //convert the owner object to a CustomUser and save
        if (owner instanceof CustomUser) {
            this.owner = (CustomUser) owner;
        } else if (owner instanceof User) {
            this.owner = new CustomUser((User) owner);
        //if we don't get a User or CustomUser object throw exception
        } else {
            throw new IllegalArgumentException("passed user object of incompatible type to Box constructor (must be User or CustomUser)");
        }

        //open a Box with either a String or CustomUser object
        if (object instanceof String) {
            this.items = new ArrayList<>();
            this.items.add((String) object);
            this.users = new ArrayList<>();
        } else if (object instanceof CustomUser) {
            this.users = new ArrayList<>();
            this.users.add((CustomUser) object);
            this.items = new ArrayList<>();
        //allow for passing of User objects, and if so convert to CustomUser
        } else if (object instanceof User) {
            this.users = new ArrayList<>();
            CustomUser user = new CustomUser((User) object);
            this.users.add(user);
            this.items = new ArrayList<>();
        } else {
            throw new IllegalArgumentException("passed added object of incompatible type to Box constructor (must be String, User or CustomUser)");
        }
    }

    //for jackson to use when parsing
    public Box() {}

    public void add(Object object) {
        if (object instanceof String) {
            this.items.add((String) object);
        } else if (object instanceof CustomUser) {
            this.users.add((CustomUser) object);
        } else if (object instanceof User) {
            this.users.add(new CustomUser((User) object));
        } else {
            throw new IllegalArgumentException("passed object of incompatible type to Box#add(Object object), must be String, User or CustomUser");
        }
    }

    public void remove(Object object) {
        if (object instanceof String) {
            this.items.remove(object);
        } else if (object instanceof CustomUser) {
            this.users.remove(object);
        } else if (object instanceof User) {
            this.users.remove(new CustomUser((User) object));
        } else {
            throw new IllegalArgumentException("passed object of incompatible type to Box#remove(Object object), must be String, User or CustomUser");
        }
    }

    public void addToBoxOfUser(Object owner, Object item) {
        if (owner instanceof User) {
            owner = new CustomUser((User) owner);
        } else if (!(owner instanceof CustomUser)) {
            throw new IllegalArgumentException("passed object of incompatible type to \"owner\" parameter of Box#addToBoxOfUser(Object owner, Object item), must be User or CustomUser");
        }
    }
}
