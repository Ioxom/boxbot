package ca.ioxom.boxbot;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JacksonYeehawHelper {
    private static final ObjectMapper mapper = new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter());

    public static void save(HashMap<Long, Box> map) {
        FrickYouJackson yeehaw = new FrickYouJackson(map);
        try {
            mapper.writeValue(new File("box_data.json"), yeehaw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Long, Box> read() {
        FrickYouJackson data = null;
        try {
            data = mapper.readValue(new File("box_data.json"), FrickYouJackson.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data == null) {
            Main.frame.throwError("failed to read json", true);
            return null;
        } else {
            return data.map;
        }
    }
}
