package io.ioxcorp.ioxbox.data.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.ioxcorp.ioxbox.data.format.Box;

import java.util.Map;

/**
 * class used to make box_data.json prettier
 * @author ioxom
 */
public final class FrickYouJackson {
    private Map<Long, Box> boxes;

    @JsonCreator
    public FrickYouJackson() {

    }

    public FrickYouJackson(final Map<Long, Box> boxes) {
        this.boxes = boxes;
    }

    public Map<Long, Box> getBoxes() {
        return this.boxes;
    }
}
