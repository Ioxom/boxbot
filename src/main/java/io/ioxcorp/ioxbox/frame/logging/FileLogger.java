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

/**
 * a class with methods to log to a {@link File file}
 * used by the {@link io.ioxcorp.ioxbox.frame.Frame} class to save logs to {@link File files}
 * @author ioxom
 */
public class FileLogger {
    private final File file;

    /**
     * creates a new {@link FileLogger} that writes to a {@link File file} in /logs/ with a filename corresponding to the date and program run
     * <br>note: if you create multiple of these in one run of a program it will generate multiple log files, avoid creating more than one
     * @author ioxom
     */
    public FileLogger() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        //filenames are log_day-month-year_run[runNumber]
        String fileName = getValidLogName();
        //create new file object
        this.file = new File("logs/" + fileName);

        //if the logs folder doesn't exist create it
        //this prevents FileNotFoundExceptions
        boolean createdFolder = false;
        if (!Files.exists(Paths.get("logs"))) {
            createdFolder = new File("logs/").mkdirs();
        }

        //log the name of the file
        try {
            if (this.file.createNewFile()) {
                System.out.println("file created: " + this.file.getName() + (createdFolder? "\ncreated logs folder" : ""));
            } else {
                System.out.println("log file already existed, using old file.");
            }
        //can't think of a case when this will be thrown so we should be fine
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.write("[log #" + this.getRun() + " on " + date + "]");
    }

    /**
     * gets a valid name for the log {@link File file} by iterating over names
     * @return a {@link String} filename in the format of log_day-month-year_run[runNumber]
     * @author ioxom
     */
    public String getValidLogName() {
        //get the date
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        //iterate over file names until we have a valid one
        for (int i = 0; true; i ++) {
            String fileName = "log_" + date + "_run" + i + ".txt";
            File f = new File("logs/" + fileName);
            if (!f.exists()) {
                return fileName;
            }
        }
    }

    /**
     * convenience method to get the run number of the program
     * @return the run number of the program starting at 0
     * @author ioxom
     */
    public int getRun() {
        return Integer.parseInt((this.file.getName().split("run"))[1].split(".txt")[0]);
    }

    /**
     * gets the name of the file in /logs/ being used
     * @return the name of the {@link File} that this logger is writing to
     */
    public String getFileName() {
        return this.file.getName();
    }

    /**
     * writes the specified message the {@link File} associated with this {@link FileLogger}
     * @param message the message to write
     * @author ioxom
     */
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
     * handles all {@link LogType LogTypes} excluding {@link LogType#CMD}
     * @param type the type of log to display before the sent message
     * @param message the message to be displayed after the type
     * @author ioxom
     */
    public void handleNormalLogs(LogType type, String message) {
        switch (type) {
            case INIT:
                this.write("\n[init] " + message);
                break;
            case MAIN:
                this.write("\n[main] " + message);
                break;
            case ERR:
                this.write("\n[err] " + message);
                break;
            case FATAL_ERR:
                this.write("\n[err/FATAL] " + message + "; closing ioxbox\n[err/FATAL] you can read this message in " + this.file.getName());
                break;
        }
    }

    /**
     * @param type a {@link LogType} that tells what to put before the message<br>note: this will throw {@link IllegalArgumentException} if you pass {@link LogType#CMD} without using the method where a user is passed
     * @param message the message to be printed to the file
     * @author ioxom
     */
    public void log(LogType type, String message) {
        if (type == LogType.CMD) {
            throw new IllegalArgumentException("cannot execute case of CMD without author information, use Frame#log(LogType, String, Object)");
        } else {
            this.handleNormalLogs(type, message);
        }
    }

    /**
     * supplementary method for {@link FileLogger#log(LogType, String)} that provides a user object, allowing {@link LogType#CMD} to be used
     * @author ioxom
     */
    public void log(LogType type, String message, Object author) {
        if (type == LogType.CMD) {
            if (author instanceof User) {
                author = new CustomUser((User) author);
            }

            if (author instanceof CustomUser) {
                this.write("\n[cmd] " + ((CustomUser) author).getTag() + " used " + message);
            } else {
                throw new IllegalArgumentException("object \"author\" passed to FileLogger#log(LogType type, String message, Object author) must be a User or CustomUser");
            }
        }
        this.handleNormalLogs(type, message);
    }
}