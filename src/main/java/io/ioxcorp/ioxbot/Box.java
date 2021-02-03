package io.ioxcorp.ioxbot;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

    //TODO: 0.2.0: this is bad
    public String toString() {
        StringBuilder users = new StringBuilder();
        if (this.users.isEmpty()) {
            users.append("none").append("\n\n");
        } else {
            for (CustomUser user : this.users) {
                users.append(user.toString()).append("\n\n");
            }
        }

        StringBuilder items = new StringBuilder();
        if (this.items.isEmpty()) {
            items.append("none").append("\n\n");
        } else {
            for (String item : this.items) {
                items.append(item).append("\n\n");
            }
        }

        return this.owner.toString() + "\nusers:\n" + users + "\nitems:\n" + items;
    }

    public MessageEmbed embed() {
        StringBuilder users = new StringBuilder();
        if (this.users.isEmpty()) {
            users.append("none").append("\n");
        } else {
            for (CustomUser user : this.users) {
                users.append(user.toString()).append("\n");
            }
        }

        StringBuilder items = new StringBuilder();
        if (this.items.isEmpty()) {
            items.append("none").append("\n");
        } else {
            for (String item : this.items) {
                items.append(item).append("\n");
            }
        }

        EmbedBuilder e = new EmbedBuilder()
                .setColor(0x00FF00)
                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                .setTitle(this.owner.tag() + "'s box contents:")
                .addField("items:", String.valueOf(items), false)
                .addField("users:", String.valueOf(users), false)
                .setFooter("box id: " + this.owner.id);

        return e.build();
    }

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
    
    public static void createBox(Object owner, Object item) {
        if (owner instanceof User) {
            owner = new CustomUser((User) owner);
        } else if (!(owner instanceof CustomUser)) {
            throw new IllegalArgumentException("passed object of incompatible type to \"owner\" parameter of Box#createBox(Object owner, Object item), must be User or CustomUser");
        }
        
        Main.boxes.put(((CustomUser) owner).id, new Box(owner, item));
    }

    public void addToBoxOfUser(Object owner, Object item) {
        if (owner instanceof User) {
            owner = new CustomUser((User) owner);
        } else if (!(owner instanceof CustomUser)) {
            throw new IllegalArgumentException("passed object of incompatible type to \"owner\" parameter of Box#addToBoxOfUser(Object owner, Object item), must be User or CustomUser");
        }
        
        if (((CustomUser) owner).hasBox()) {
            Main.boxes.get(((CustomUser) owner).id).add(item);   
        } else {
            createBox(owner, item);
        }
    }

    public void removeFromBoxOfUser(Object owner, Object item) {
        if (owner instanceof User) {
            owner = new CustomUser((User) owner);
        } else if (!(owner instanceof CustomUser)) {
            throw new IllegalArgumentException("passed object of incompatible type to \"owner\" parameter of Box#removeFromBoxOfUser(Object owner, Object item), must be User or CustomUser");
        }

        //TODO: 0.2.0: NullPointerException is the wrong thing to throw here
        if (!((CustomUser) owner).hasBox()) {
            throw new NullPointerException("\"owner\" object passed to Box#removeFromBoxOfUser does not have a box to remove from");
        } else if (!Main.boxes.get(((CustomUser) owner).id).contains(item)) {
            throw new NullPointerException("Box of owner does not contain requested item");
        }

        Main.boxes.get(((CustomUser) owner).id).remove(item);
    }

    public boolean contains(Object object) {
        if (object instanceof CustomUser) {
            return this.users.contains(object);
        } else if (object instanceof String) {
            return this.items.contains(object);
        } else {
            return false;
        }
    }
}
