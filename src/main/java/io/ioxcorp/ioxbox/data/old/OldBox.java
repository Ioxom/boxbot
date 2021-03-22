package io.ioxcorp.ioxbox.data.old;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.CustomUser;

import java.util.ArrayList;
import java.util.List;

public final class OldBox {
    /**
     * the owner of the box
     */
    @JsonProperty("owner")
    private final OldCustomUser owner;

    /**
     * an {@link ArrayList} of {@link String Strings}: the items in the box
     */
    @JsonProperty("items")
    private final List<String> items;

    /**
     * an {@link ArrayList} of {@link String Strings}: the items in the box
     */
    @JsonProperty("users")
    private List<OldCustomUser> users;

    @JsonCreator
    public OldBox() {
        owner = new OldCustomUser();
        items = new ArrayList<>();
        users = new ArrayList<>();
    }

    @JsonSetter
    public void setUsers(final List<OldCustomUser> list) {
        this.users = list;
    }

    @JsonGetter
    public OldCustomUser getOwner() {
        return owner;
    }

    @JsonGetter
    public List<String> getItems() {
        return items;
    }

    @JsonGetter
    public List<OldCustomUser> getUsers() {
        return users;
    }

    public Box convert() {
        List<CustomUser> convertedUsers = new ArrayList<>();
        for (final OldCustomUser user : this.users) {
            convertedUsers.add(new CustomUser(user));
        }
        return new Box(new CustomUser(owner), convertedUsers, items);
    }
}
