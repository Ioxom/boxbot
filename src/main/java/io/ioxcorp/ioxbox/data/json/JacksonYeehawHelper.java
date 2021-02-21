package io.ioxcorp.ioxbox.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.frame.logging.LogType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class JacksonYeehawHelper {

    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * saves the data currently stored in {@link Main#boxes} to box_data.json
     * if box_data.json doesn't exist create it
     * @author ioxom
     */
    public static void save() {
        FrickYouJackson yeehaw = new FrickYouJackson(Main.boxes);

        File file = new File("box_data.json");
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                mapper.writeValue(file, yeehaw);

                if (created) {
                    Main.frame.log(LogType.MAIN ,"created new file: " + file.getName() + " and saved the stored data to it");
                }
            } else {
                mapper.writeValue(file, yeehaw);
            }
        } catch(IOException e) {
            Main.frame.log(LogType.ERR, "failed to write to box_data.json: " + e);
        }
    }

    /**
     * gets the stored data in box_data.json
     * @return a {@link HashMap<>} of the stored boxes in box_data.json
     * @author ioxom
     */
    public static HashMap<Long, Box> read() {
        File file = new File("box_data.json");

        //if the file doesn't exist create it and return an empty HashMap
        if (file.exists()) {
            try {
                boolean created = file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("{\n   \n}");
                writer.close();

                if (created) {
                    Main.frame.log(LogType.MAIN ,"created new file: " + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new HashMap<>();
        } else {
            FrickYouJackson data = null;
            try {
                data = mapper.readValue(file, FrickYouJackson.class);
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
}