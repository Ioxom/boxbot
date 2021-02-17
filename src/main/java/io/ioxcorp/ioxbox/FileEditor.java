package io.ioxcorp.ioxbox;

import io.ioxcorp.ioxbox.frame.logging.LogType;
import static io.ioxcorp.ioxbox.Main.frame;

import java.io.FileWriter;
import java.io.IOException;


public class FileEditor {
    public static FileWriter myWriter;

    static {
        try {
            myWriter = new FileWriter("files/loggers.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main() throws IOException {
            FileWriter myWriter = new FileWriter("files/loggers.txt", true);
            myWriter.write("e");
            myWriter.close();
            frame.log(LogType.INIT, "Successfully wrote command to file");
    }
}