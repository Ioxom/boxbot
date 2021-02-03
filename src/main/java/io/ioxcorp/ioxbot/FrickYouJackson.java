package io.ioxcorp.ioxbot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
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