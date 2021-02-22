package io.ioxcorp.ioxbox.data.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.ioxcorp.ioxbox.data.format.Box;

import java.util.HashMap;

public class FrickYouJackson {
    public HashMap<Long, Box> map;

    @JsonCreator
    public FrickYouJackson() {}

    public FrickYouJackson(HashMap<Long, Box> map) {
        this.map = map;
    }
}