package io.ioxcorp.ioxbox.data.old;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import io.ioxcorp.ioxbox.data.format.Box;

import java.util.HashMap;

public final class OldFrickYouJackson {
    private final HashMap<Long, OldBox> boxes;

    @JsonCreator
    public OldFrickYouJackson() {
        this.boxes = new HashMap<>();
    }

    public HashMap<Long, Box> convert() {
        HashMap<Long, Box> convertedData = new HashMap<>();
        for (long key : boxes.keySet()) {
            convertedData.put(key, boxes.get(key).convert());
        }
        return convertedData;
    }

    @JsonGetter
    public HashMap<Long, OldBox> getBoxes() {
        return boxes;
    }
}
