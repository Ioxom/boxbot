package io.ioxcorp.ioxbox.data.format;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

//this is used so we can clean up the JSON file a bit
public class FrickYouJackson {
    @JsonProperty("boxes")
    public HashMap<Long, Box> map;

    @JsonCreator
    public FrickYouJackson() {}

    public FrickYouJackson(HashMap<Long, Box> map) {
        this.map = map;
    }
}