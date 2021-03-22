package io.ioxcorp.ioxbox.data.old;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import io.ioxcorp.ioxbox.data.format.Box;

import java.util.HashMap;
import java.util.Map;

/**
 * used for converting old data from box_data.json to the newer format
 */
public final class OldFrickYouJackson {
    private final Map<Long, OldBox> boxes;

    @JsonCreator
    public OldFrickYouJackson() {
        this.boxes = new HashMap<>();
    }

    public Map<Long, Box> convert() {
        Map<Long, Box> convertedData = new HashMap<>();
        for (final Map.Entry<Long, OldBox> entry : boxes.entrySet()) {
            convertedData.put(entry.getKey(), entry.getValue().convert());
        }
        return convertedData;
    }

    @JsonGetter
    public Map<Long, OldBox> getBoxes() {
        return boxes;
    }
}
