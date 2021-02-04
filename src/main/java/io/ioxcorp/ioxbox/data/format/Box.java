package io.ioxcorp.ioxbox.data.format;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.json.JacksonYeehawHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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

    public Box(Object owner) {
        //convert the owner object to a CustomUser and save
        if (owner instanceof CustomUser) {
            this.owner = (CustomUser) owner;
        } else if (owner instanceof User) {
            this.owner = new CustomUser((User) owner);
            //if we don't get a User or CustomUser object throw exception
        } else {
            throw new IllegalArgumentException("passed user object of incompatible type to Box constructor (must be User or CustomUser)");
        }

        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    @JsonCreator
    public Box() {}

    public String toString() {
        StringBuilder users = new StringBuilder();
        if (this.users.isEmpty()) {
            users.append("none").append("\n\n");
        } else {
            for (int i = 0; i < this.users.size(); i ++) {
                if (i == this.users.size() - 1) {
                    users.append(this.users.get(i).toString()).append("\n\n");
                } else {
                    users.append(this.users.get(i).toString()).append(",\n");
                }
            }
        }

        StringBuilder items = new StringBuilder();
        if (this.items.isEmpty()) {
            items.append("none").append("\n\n");
        } else {
            for (int i = 0; i < this.items.size(); i ++) {
                if (i == this.items.size() - 1) {
                    users.append(this.items.get(i)).append("\n\n");
                } else {
                    users.append(this.items.get(i)).append(",\n");
                }
            }
        }

        return this.owner.getTag() + "'s box:\nusers:\n" + users + "\nitems:\n" + items;
    }

    public MessageEmbed embed() {
        StringBuilder users = new StringBuilder();
        if (this.users.isEmpty()) {
            users.append("none").append("\n");
        } else {
            for (int i = 0; i < this.users.size(); i ++) {
                if (i == this.users.size() - 1) {
                    users.append(this.users.get(i).toString()).append("\n\n");
                } else {
                    users.append(this.users.get(i).toString()).append(",\n");
                }
            }
        }

        StringBuilder items = new StringBuilder();
        if (this.items.isEmpty()) {
            items.append("none").append("\n");
        } else {
            for (int i = 0; i < this.items.size(); i ++) {
                if (i == this.items.size() - 1) {
                    items.append(this.items.get(i)).append("\n\n");
                } else {
                    items.append(this.items.get(i)).append(",\n");
                }
            }
        }

        EmbedBuilder e = new EmbedBuilder()
                .setColor(0x00FF00)
                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                .setTitle(this.owner.getTag() + "'s box contents:")
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

        JacksonYeehawHelper.save();
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

        JacksonYeehawHelper.save();
    }
    
    public static void createBox(Object owner, Object item) throws IOException {
        if (owner instanceof User) {
            owner = new CustomUser((User) owner);
        } else if (!(owner instanceof CustomUser)) {
            throw new IllegalArgumentException("passed object of incompatible type to \"owner\" parameter of Box#createBox(Object owner, Object item), must be User or CustomUser");
        }

        if (((CustomUser) owner).hasBox()) {
            throw new IOException();
        }

        Main.boxes.put(((CustomUser) owner).id, new Box(owner, item));

        JacksonYeehawHelper.save();
    }

    public static void createBox(Object owner) throws IOException {
        if (owner instanceof User) {
            owner = new CustomUser((User) owner);
        } else if (!(owner instanceof CustomUser)) {
            throw new IllegalArgumentException("passed object of incompatible type to \"owner\" parameter of Box#createBox(Object owner), must be User or CustomUser");
        }

        if (((CustomUser) owner).hasBox()) {
            throw new IOException();
        }

        Main.boxes.put(((CustomUser) owner).id, new Box(owner));

        JacksonYeehawHelper.save();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return Objects.equals(owner, box.owner) && Objects.equals(items, box.items) && Objects.equals(users, box.users);
    }
}
