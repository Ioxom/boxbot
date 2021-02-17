package io.ioxcorp.ioxbox;

import java.io.File;
import java.io.IOException;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import static io.ioxcorp.ioxbox.Main.frame;

public class FileCreator {
    public static void main() {
        File file = new File("files/loggers.txt");
        try {
            if (file.createNewFile()) {
                frame.log(LogType.INIT, "Successfully created the log file");
            } else {
                frame.log(LogType.INIT, "log file already existed, nothing happened");
            }
        } catch (IOException e) {
            frame.log(LogType.ERROR, e.getMessage());
        }
    }
}
