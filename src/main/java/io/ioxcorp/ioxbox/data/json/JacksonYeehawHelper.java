package io.ioxcorp.ioxbox.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.old.OldFrickYouJackson;
import io.ioxcorp.ioxbox.frame.logging.LogType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public final class JacksonYeehawHelper {
    private JacksonYeehawHelper() {

    }

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * saves the data currently stored in {@link Main#BOXES} to box_data.json.
     * if box_data.json doesn't exist create it
     * @author ioxom
     */
    public static void save() {
        FrickYouJackson yeehaw = new FrickYouJackson(Main.BOXES);

        File file = new File("box_data.json");
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                MAPPER.writeValue(file, yeehaw);

                if (created) {
                    Main.FRAME.log(LogType.MAIN, "created new file: " + file.getName() + " and saved the stored data to it");
                }
            } else {
                MAPPER.writeValue(file, yeehaw);
            }
        } catch (IOException e) {
            Main.FRAME.log(LogType.ERR, "failed to write to box_data.json: " + e);
        }
    }

    /**
     * gets the stored data in box_data.json
     * @return a {@link HashMap<>} of the stored boxes in box_data.json or an empty hashmap if the file is empty or not found
     * @author ioxom
     */
    public static HashMap<Long, Box> read() {
        File file = new File("box_data.json");

        //if the file doesn't exist create it and return an empty HashMap
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("{\n   \n}");
                writer.close();

                if (created) {
                    Main.FRAME.log(LogType.MAIN, "created new file: " + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new HashMap<>();
        } else {
            FrickYouJackson data = null;
            //changing the name of "tag" to "discriminator" broke some data, this should fix it
            //this is immediately fixed after running save() once
            try {
                OldFrickYouJackson oldData = MAPPER.readValue(file, OldFrickYouJackson.class);
                return oldData.convert();
            } catch (Exception ignored) {
                //if we get an exception we know that the data is in the correct format
            }
            try {
                data = MAPPER.readValue(file, FrickYouJackson.class);
            } catch (IOException e) {
                Main.FRAME.log(LogType.FATAL_ERR, "failed to read box_data.json: "  + e);
            }

            if (data == null) {
                Main.FRAME.log(LogType.FATAL_ERR, "failed to read json data for unknown reasons");
                return new HashMap<>();
            } else {
                if (data.getBoxes() == null) {
                    return new HashMap<>();
                }
                return data.getBoxes();
            }
        }
    }
}
