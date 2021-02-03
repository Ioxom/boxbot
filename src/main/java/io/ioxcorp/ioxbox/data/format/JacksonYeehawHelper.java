package io.ioxcorp.ioxbox.data.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.ioxcorp.ioxbox.Main;
import static io.ioxcorp.ioxbox.Frame.LogType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JacksonYeehawHelper {

    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void save() {
        FrickYouJackson yeehaw = new FrickYouJackson(Main.boxes);
        try {
            mapper.writeValue(new File("box_data.json"), yeehaw);
        } catch (IOException e) {
            //TODO: 0.2.0: replace printStackTrace() with an error thrown to frame console
            e.printStackTrace();
        }
    }

    public static HashMap<Long, Box> read() {
        FrickYouJackson data = null;
        try {
            data = mapper.readValue(new File("box_data.json"), FrickYouJackson.class);
        } catch (IOException e) {
            //TODO: 0.2.0: replace printStackTrace() with an error thrown to frame console
            e.printStackTrace();
        }

        if (data == null) {
            //TODO: 0.2.0: better error here
            Main.frame.log(LogType.FATAL_ERROR, "failed to read json");
            return null;
        } else {
            return data.map;
        }
    }
}
