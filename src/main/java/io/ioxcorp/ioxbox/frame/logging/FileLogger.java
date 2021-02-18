package io.ioxcorp.ioxbox.frame.logging;

import io.ioxcorp.ioxbox.data.format.CustomUser;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger {
    private File file;

    /**
     * creates a new {@link FileLogger} that writes to a file in /logs/ with a filename corresponding to the date and program run
     * @author ioxom
     */
    public FileLogger() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        //get files until the name is valid
        //filenames are log_day-month-year_runRunNumber
        boolean validFileName = false;
        for (int i = 0; !validFileName; i ++) {
            String fileName = "log_" + date + "_run" + i + ".txt";
            //create new file object and check if it exists
            File f = new File("logs/" + fileName);
            if (!f.exists()) {
                validFileName = true;

                this.file = new File("logs/" + fileName);
                //if the logs folder doesn't exist create it
                //this prevents FileNotFoundExceptions
                boolean createdFolder = false;
                if (!Files.exists(Paths.get("logs"))) {
                    createdFolder = new File("logs/").mkdirs();
                }

                try {
                    if (this.file.createNewFile()) {
                        System.out.println("File created: " + this.file.getName() + (createdFolder? "\ncreated logs folder" : ""));
                    } else {
                        System.out.println("File already exists.");
                    }
                //can't think of a case when this will be thrown so we should be fine
                } catch (IOException e) {
                    e.printStackTrace();
                }

                this.write("[log #" + i + " on " + date + "]");
            }
        }
    }

    public void write(String message) {
        try {
            FileWriter writer = new FileWriter(this.file, true);
            writer.write(message);
            writer.close();
        //this should never be thrown because our file is always valid
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type a {@link LogType} that tells what to put before the message<br>note: this will throw {@link IllegalArgumentException} if you pass LogType.CMD without using the method where a user is passed
     * @param message the message to be printed to the file
     * @author ioxom
     */
    public void log(LogType type, String message) {
        switch (type) {
            case INIT:
                this.write("\n[init] " + message);
                break;
            case MAIN:
                this.write("\n[main] " + message);
                break;
            case ERROR:
                this.write("\n[err] " + message);
                break;
            case FATAL_ERROR:
                this.write("\n[err/FATAL] " + message + "; closing ioxbox");
                //wait for five seconds to allow for reading the error
                try {
                    Thread.sleep(5000);
                    System.exit(1);
                } catch (Exception e) {
                    System.exit(1);
                }
                break;
            case CMD:
                throw new IllegalArgumentException("cannot execute case of CMD without author information, use FileLogger#log(LogType, String, Object)");
        }
    }

    /**
     * supplementary method for {@link FileLogger#log(LogType, String)} that provides a user object, allowing CMD to be used
     * @author ioxom
     */
    public void log(LogType type, String message, Object author) {
        switch (type) {
            case INIT:
                this.write("\n[init] " + message);
                break;
            case MAIN:
                this.write("\n[main] " + message);
                break;
            case ERROR:
                this.write("\n[err] " + message);
                break;
            case FATAL_ERROR:
                this.write("\n[err/FATAL] " + message + "; closing ioxbox");
                //wait for five seconds to allow for reading the error
                try {
                    Thread.sleep(5000);
                    System.exit(1);
                } catch (Exception e) {
                    System.exit(1);
                }
                break;
            case CMD:
                if (author instanceof User) {
                    author = new CustomUser((User) author);
                }

                if (author instanceof CustomUser) {
                    this.write("\n[cmd] " + ((CustomUser) author).getTag() + " used " + message);
                } else {
                    throw new IllegalArgumentException("object \"author\" passed to FileLogger#log(LogType type, String message, Object author) must be a User or CustomUser");
                }
        }
    }
}