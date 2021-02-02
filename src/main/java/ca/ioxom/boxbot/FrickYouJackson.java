package ca.ioxom.boxbot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FrickYouJackson {
    @JsonProperty("boxes")
    public Box[] boxes;
    public FrickYouJackson() {}

    public FrickYouJackson(Box[] boxes) {
        this.boxes = boxes;
    }
}