package io.ioxcorp.ioxbox.data.old;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class OldCustomUser {
    @JsonProperty("id")
    private final long id;
    @JsonProperty("username")
    private final String username;
    @JsonProperty("tag")
    private final int tag;

    @JsonCreator
    public OldCustomUser() {
        this.id = 0L;
        this.username = "";
        this.tag = 0;
    }

    @JsonGetter
    public String getUsername() {
        return username;
    }

    @JsonGetter
    public long getId() {
        return this.id;
    }

    @JsonGetter
    public int getDiscriminator() {
        return this.tag;
    }
}
