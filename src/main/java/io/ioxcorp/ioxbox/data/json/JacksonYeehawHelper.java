package io.ioxcorp.ioxbox.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import io.ioxcorp.ioxbox.frame.logging.LogType;

public class JacksonYeehawHelper {

    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void save() {
        FrickYouJackson yeehaw = new FrickYouJackson(Main.boxes);
        try {
            mapper.writeValue(new File("box_data.json"), yeehaw);
        } catch (IOException e) {
            Main.frame.log(LogType.ERR, "failed to write to box_data.json: " + e);
        }
    }

    public static HashMap<Long, Box> read() {
        String fileName = "box_data.json";

        //if the file doesn't exist create it
        if (!Files.exists(Paths.get(fileName))) {
            File file = new File(fileName);
            boolean created = false;
            try {
                created = file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("{\n   \n}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (created) {
                Main.frame.log(LogType.INIT ,"created new file: " + fileName);
            }
            return new HashMap<>();
        }

        FrickYouJackson data = null;
        try {
            data = mapper.readValue(new File(fileName), FrickYouJackson.class);
        } catch (IOException e) {
            Main.frame.log(LogType.FATAL_ERR, "failed to read box_data.json: "  + e);
        }

        if (data == null) {
            Main.frame.log(LogType.FATAL_ERR, "failed to read json data for unknown reasons");
            return null;
        } else {
            return data.map;
        }
    }
}