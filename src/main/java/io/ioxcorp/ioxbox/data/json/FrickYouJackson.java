package io.ioxcorp.ioxbox.data.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.ioxcorp.ioxbox.data.format.Box;

import java.util.HashMap;

public final class FrickYouJackson {
    private HashMap<Long, Box> boxes;

    @JsonCreator
    public FrickYouJackson() {

    }

    public FrickYouJackson(final HashMap<Long, Box> boxes) {
        this.boxes = boxes;
    }

    public HashMap<Long, Box> getBoxes() {
        return this.boxes;
    }
}
