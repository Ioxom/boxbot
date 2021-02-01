package ca.ioxom.boxbot;

import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class Box {
    public CustomUser owner;
    public ArrayList<String> items;
    public ArrayList<CustomUser> users;
    public Box(Object owner, Object object) {
        if (owner instanceof CustomUser) {
            this.owner = (CustomUser) owner;
        } else if (owner instanceof User) {
            this.owner = new CustomUser((User) owner);
        } else {
            throw new IllegalArgumentException("passed user object of incompatible type to constructor");
        }
        if (object instanceof String) {
            this.items = new ArrayList<>();
            this.items.add((String) object);
            this.users = new ArrayList<>();
        } else if (object instanceof CustomUser) {
            this.users = new ArrayList<>();
            this.users.add((CustomUser) object);
            this.items = new ArrayList<>();
        } else {
            throw new IllegalArgumentException("passed added object of incompatible type to constructor");
        }
    }

    public void add(Object object) {
        if (object instanceof String) {
            this.items.add((String) object);
        } else if (object instanceof CustomUser) {
            this.users.add((CustomUser) object);
        } else {
            throw new IllegalArgumentException("passed object of incompatible type to Box#add(Object object)");
        }
    }

    public void remove(Object object) {
        if (object instanceof String) {
            this.items.remove(object);
        } else if (object instanceof CustomUser) {
            this.users.remove(object);
        } else {
            throw new IllegalArgumentException("passed object of incompatible type to Box#remove(Object object)");
        }
    }

    public void addToBoxOfUser(CustomUser owner, Object item) {

    }
}
