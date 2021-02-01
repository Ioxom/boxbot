package ca.ioxom.boxbot;

import java.util.HashMap;

public class FrickYouJackson {
    public HashMap<String, Box> boxes;
    public FrickYouJackson() {
        this.boxes = new HashMap<>();
    }

    public FrickYouJackson(HashMap<String, Box> boxes) {
        this.boxes = boxes;
    }
}