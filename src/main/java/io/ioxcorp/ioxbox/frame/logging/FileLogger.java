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

    public FileLogger() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        //get files until the name is valid
        boolean validFileName = false;
        for (int i = 0; !validFileName; i ++) {
            String fileName = "log_" + date + "_run" + i + ".txt";
            File f = new File("logs/" + fileName);
            if (!f.exists()) {
                validFileName = true;
                this.file = new File("logs/" + fileName);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        }
    }

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