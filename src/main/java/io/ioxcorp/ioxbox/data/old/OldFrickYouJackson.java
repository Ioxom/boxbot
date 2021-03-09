package io.ioxcorp.ioxbox.data.old;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import io.ioxcorp.ioxbox.data.format.Box;

import java.util.HashMap;

/**
 * used for converting old data from box_data.json to the newer format
 */
public final class OldFrickYouJackson {
    private final HashMap<Long, OldBox> boxes;

    @JsonCreator
    public OldFrickYouJackson() {
        this.boxes = new HashMap<>();
    }

    public HashMap<Long, Box> convert() {
        HashMap<Long, Box> convertedData = new HashMap<>();
        for (final long key : boxes.keySet()) {
            convertedData.put(key, boxes.get(key).convert());
        }
        return convertedData;
    }

    @JsonGetter
    public HashMap<Long, OldBox> getBoxes() {
        return boxes;
    }
}
