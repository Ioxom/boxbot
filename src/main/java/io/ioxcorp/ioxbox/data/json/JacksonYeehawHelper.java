package io.ioxcorp.ioxbox.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static io.ioxcorp.ioxbox.Frame.LogType;

public class JacksonYeehawHelper {

    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void save() {
        FrickYouJackson yeehaw = new FrickYouJackson(Main.boxes);
        try {
            mapper.writeValue(new File("box_data.json"), yeehaw);
        } catch (IOException e) {
            Main.frame.log(LogType.ERROR, "failed to write to box_data.json: " + e);
        }
    }

    public static HashMap<Long, Box> read() {
        FrickYouJackson data = null;
        try {
            data = mapper.readValue(new File("box_data.json"), FrickYouJackson.class);
        } catch (IOException e) {
            Main.frame.log(LogType.FATAL_ERROR, "failed to read box_data.json: "  + e);
        }

        if (data == null) {
            Main.frame.log(LogType.FATAL_ERROR, "failed to read json data for unknown reasons");
            return null;
        } else {
            return data.map;
        }
    }
}