package ca.ioxom.boxbot;

import com.fasterxml.jackson.annotation.JsonProperty;

//this is used so we can clean up the JSON file a bit
public class FrickYouJackson {
    @JsonProperty("boxes")
    public Box[] boxes;

    //empty constructor for jackson to use when saving from JSON
    public FrickYouJackson() {}

    public FrickYouJackson(Box[] boxes) {
        this.boxes = boxes;
    }
}