package io.ioxcorp.ioxbox.data.format;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.json.JacksonYeehawHelper;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * a container for items (instances of {@link String} and {@link CustomUser}.
 */
public final class Box {

    /**
     * the owner of the box
     */
    @JsonProperty("owner")
    private final CustomUser owner;

    /**
     * an {@link ArrayList} of {@link String Strings}: the items in the box
     */
    @JsonProperty("items")
    private final ArrayList<String> items;

    /**
     * an {@link ArrayList} of {@link String Strings}: the items in the box
     */
    @JsonProperty("users")
    private final ArrayList<CustomUser> users;

    /**
     * the main constructor for {@link Box Box}; creates a new box with the specified item inside and the specified user as owner
     * @param owner must be a {@link CustomUser CustomUser} or {@link User User}; the owner of the box
     * @param object must be a {@link String String}, {@link CustomUser CustomUser} or {@link User User}; the object that is inside the newly created box
     * @exception IllegalArgumentException if the owner or object parameters are incompatible types
     * @author ioxom
     */
    public Box(final Object owner, final Object object) {
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

    /**
     * a stripped-down version of {@link Box#Box(Object, Object)} that creates an empty box
     * @param owner must be a {@link io.ioxcorp.ioxbox.data.format.CustomUser CustomUser} or {@link net.dv8tion.jda.api.entities.User User}; the owner of the box
     * @exception IllegalArgumentException if the owner object passed is an incompatible type
     * @see Box#Box(Object, Object)
     * @author ioxom
     */
    public Box(final Object owner) {
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

    /**
     * do not use this, it is a constructor used only for jackson to be able to parse Box objects from json
     * @author ioxom
     */
    @JsonCreator
    public Box() {
        this.owner = null;
        this.items = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public String toString() {
        return this.owner.getAsTag() + "'s box:\nusers:\n" + this.usersToString() + "\nitems:\n" + this.itemsToString();
    }

    /**
     * a method for making a discord-sendable representation of a box
     * @return a {@link net.dv8tion.jda.api.entities.MessageEmbed MessageEmbed} that can be sent in discord showing the contents of the referenced box
     * @author ioxom
     */
    public MessageEmbed embed() {
        EmbedBuilder e = new EmbedBuilder()
                .setColor(EmbedHelper.SUCCESS_EMBED_COLOUR)
                .setAuthor("ioxbox", "https://ioxom.github.io/ioxbox/", "https://raw.githubusercontent.com/Ioxom/ioxbox/master/src/main/resources/images/box.png")
                .setTitle(this.owner.getAsTag() + "'s box contents:")
                .addField("items:", this.itemsToString(), false)
                .addField("users:", this.usersToString(), false)
                .setFooter("box id: " + this.owner.getId());

        return e.build();
    }

    /**
     * @return a readable {@link java.lang.String String} of the items in the specified box
     * @author ioxom
     */
    public String itemsToString() {
        StringBuilder itemsAsString = new StringBuilder();
        if (this.items.isEmpty()) {
            itemsAsString.append("none").append("\n");
        } else {
            for (int i = 0; i < this.items.size(); i++) {
                if (i == this.items.size() - 1) {
                    itemsAsString.append(this.items.get(i)).append("\n\n");
                } else {
                    itemsAsString.append(this.items.get(i)).append(",\n");
                }
            }
        }

        return itemsAsString.toString();
    }

    /**
     * @return a readable {@link java.lang.String String} of the users in the specified box
     * @author ioxom
     */
    public String usersToString() {
        StringBuilder usersAsString = new StringBuilder();
        if (this.users.isEmpty()) {
            usersAsString.append("none").append("\n");
        } else {
            for (int i = 0; i < this.users.size(); i++) {
                if (i == this.users.size() - 1) {
                    usersAsString.append("user ").append(i).append(":\n").append(this.users.get(i).toString()).append("\n\n");
                } else {
                    usersAsString.append("user ").append(i).append(":\n").append(this.users.get(i).toString()).append(",\n");
                }
            }
        }

        return usersAsString.toString();
    }

    /**
     * adds the Object passed to the {@link io.ioxcorp.ioxbox.data.format.Box} specified if it is a compatible format, then saves to json
     * @param object a {@link java.lang.String String}, a {@link io.ioxcorp.ioxbox.data.format.CustomUser CustomUser} or {@link net.dv8tion.jda.api.entities.User User} to be added to the referenced {@link io.ioxcorp.ioxbox.data.format.Box}
     * @exception IllegalArgumentException if the object passed in an incompatible type
     * @see Box#remove(Object)
     * @author ioxom
     */
    public void add(final Object object) {
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

    /**
     * removes the Object passed to the {@link io.ioxcorp.ioxbox.data.format.Box} specified if it is a compatible format, then saves to json
     * @param object a {@link java.lang.String String}, a {@link io.ioxcorp.ioxbox.data.format.CustomUser CustomUser} or {@link net.dv8tion.jda.api.entities.User User} to be removed from the referenced {@link io.ioxcorp.ioxbox.data.format.Box}
     * @exception IllegalArgumentException if the object passed in an incompatible type
     * @see Box#add(Object)
     * @author ioxom
     */
    public void remove(final Object object) {
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

    /**
     * adds a new {@link Box Box} to the {@link java.util.HashMap HashMap} created in Main with one item (item parameter), and saves to json
     * @param owner must be a {@link io.ioxcorp.ioxbox.data.format.CustomUser CustomUser} or {@link net.dv8tion.jda.api.entities.User User}, will be used as the owner of the box
     * @param item a {@link java.lang.String String}, {@link io.ioxcorp.ioxbox.data.format.CustomUser CustomUser} or {@link net.dv8tion.jda.api.entities.User User} to be the initial item in the box
     * @exception IllegalArgumentException if the owner or item objects are incompatible types
     * @exception InvalidParameterException if the owner already has a box
     * @see Box#createBox(CustomUser)
     * @author ioxom
     */
    public static void createBox(final CustomUser owner, final Object item) {
        if (owner.hasBox()) {
            throw new InvalidParameterException("user in \"owner\" passed as argument already has a box; could not create new one");
        }

        Main.BOXES.put(owner.getId(), new Box(owner, item));

        JacksonYeehawHelper.save();
    }

    /**
     * adds a new empty {@link Box Box} to the {@link java.util.HashMap HashMap} created in Main, and saves to json
     * @param owner must be a {@link io.ioxcorp.ioxbox.data.format.CustomUser CustomUser} or {@link net.dv8tion.jda.api.entities.User User}, will be used as the owner of the box
     * @exception IllegalArgumentException if the owner or item objects are incompatible types
     * @exception InvalidParameterException if the owner already has a box
     * @see Box#createBox(CustomUser, Object)
     * @author ioxom
     */
    public static void createBox(final CustomUser owner) {
        if (owner.hasBox()) {
            throw new InvalidParameterException("\"user\" passed as argument already has a box; could not create new one");
        }

        Main.BOXES.put(owner.getId(), new Box(owner));

        JacksonYeehawHelper.save();
    }

    /**
     * checks if a box contains the object specified
     * @param object must be a {@link io.ioxcorp.ioxbox.data.format.CustomUser CustomUser}, {@link net.dv8tion.jda.api.entities.User User} or {@link java.lang.String}; the object to be checked for
     * @return true, if the object is found; false, if the object is not found
     * @author ioxom
     */
    public boolean contains(final Object object) {
        if (object instanceof CustomUser) {
            return this.users.contains(object);
        } else if (object instanceof String) {
            return this.items.contains(object);
        } else if (object instanceof User) {
            return this.users.contains(new CustomUser((User) object));
        } else {
            return false;
        }
    }

    @JsonGetter
    public CustomUser getOwner() {
        return owner;
    }

    @JsonGetter
    public ArrayList<String> getItems() {
        return items;
    }

    @JsonGetter
    public ArrayList<CustomUser> getUsers() {
        return users;
    }

    /**
     * overridden equals method.
     */
    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Box box = (Box) o;
        return Objects.equals(owner, box.owner) && Objects.equals(items, box.items) && Objects.equals(users, box.users);
    }

    /**
     * overridden hashCode method.
     */
    @Override
    public int hashCode() {
        return Objects.hash(owner, items, users);
    }
}
