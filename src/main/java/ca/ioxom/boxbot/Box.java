package ca.ioxom.boxbot;

import java.util.ArrayList;

public class Box {
    public CustomUser owner;
    public ArrayList<String> items;
    public ArrayList<CustomUser> users;
    public Box(CustomUser owner, Object object) {
        this.owner = owner;
        if (object instanceof String) {
            this.items = new ArrayList<>();
            this.items.add((String) object);
            this.users = new ArrayList<>();
        } else if (object instanceof CustomUser) {
            this.users = new ArrayList<>();
            this.users.add((CustomUser) object);
            this.items = new ArrayList<>();
        } else {
            throw new IllegalArgumentException("passed object of incompatible type to constructor");
        }

        System.out.println(this.items);
        System.out.println(this.users);
        System.out.println(this.owner);
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

    public void addToUsersBox(CustomUser owner, Object item) {

    }
}
