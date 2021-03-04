package io.ioxcorp.ioxbox.data.old;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;

import java.util.ArrayList;

public class OldBox {
    /**
     * the owner of the box
     */
    @JsonProperty("owner")
    private final OldCustomUser owner;

    /**
     * an {@link ArrayList} of {@link String Strings}: the items in the box
     */
    @JsonProperty("items")
    private final ArrayList<String> items;

    /**
     * an {@link ArrayList} of {@link String Strings}: the items in the box
     */
    @JsonProperty("users")
    private ArrayList<OldCustomUser> users;

    @JsonCreator
    public OldBox() {
        this.owner = new OldCustomUser();
        this.items = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    @JsonSetter
    public void setUsers(ArrayList<OldCustomUser> list) {
        this.users = list;
    }

    @JsonGetter
    public CustomUser getOwner() {
        return new CustomUser(this.owner);
    }

    @JsonGetter
    public ArrayList<String> getItems() {
        return items;
    }

    @JsonGetter
    public ArrayList<CustomUser> getUsers() {
        ArrayList<CustomUser> list = new ArrayList<>();
        for (OldCustomUser user : this.users) {
            list.add(new CustomUser(user));
        }
        return list;
    }

    public Box convert() {
        return new Box(this.getOwner(), this.getUsers(), this.getItems());
    }
}
