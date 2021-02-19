package io.ioxcorp.ioxbox;

import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.json.JacksonYeehawHelper;
import io.ioxcorp.ioxbox.frame.Frame;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetterListener;
import io.ioxcorp.ioxbox.listeners.MainListener;
import io.ioxcorp.ioxbox.listeners.status.StatusSetter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import io.ioxcorp.ioxbox.frame.logging.LogType;

public class Main {
    public static final Random random = new Random();

    //get the version from a .properties that's saved in the .jar that gradle produces
    public static String VERSION;
    static {
        try {
            Properties properties = new Properties();
            String fileName = "ioxbox.properties";
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(fileName);

            //load input stream
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file \"" + fileName + "\" not found in the classpath");
            }

            VERSION = properties.getProperty("version");
            properties.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final Frame frame = new Frame();
    static {
        frame.init();
    }

    public static final HashMap<Long, Box> boxes;
    static {
        //read saved box data
        boxes = JacksonYeehawHelper.read();
    }

    public static void main(String[] args) {

        //throw error if version is not found
        if (VERSION == null) {
            VERSION = "0.0.0";
            frame.log(LogType.ERR, "could not get version from \"ioxbox.properties\". this file should normally be stored in the .jar file that is run, but it seems an error occurred on saving.");
        }

        //log in
        JDA api = null;
        try {
            String token = getToken();
            api = JDABuilder.createDefault(token).build();
            Main.frame.log(LogType.INIT, "successfully logged in JDA");
        } catch (LoginException e) {
            frame.log(LogType.FATAL_ERR, "invalid token");
        }

        //add event listeners
        if (api != null) {
            api.addEventListener(new MainListener(), new ConfirmationGetterListener(), new StatusSetter());
            frame.log(LogType.INIT, "initialized jda");
        } else {
            frame.log(LogType.ERR, "failed to create JDA object for unknown reasons");
        }
    }

    public static String getToken() {
        String token = null;
        String fileName = "token.txt";
        try {
            if (!Files.exists(Paths.get(fileName))) {
                boolean created = new File(fileName).createNewFile();
                if (created) {
                    frame.log(LogType.FATAL_ERR, "could not find token file and created token.txt: paste in your token and rerun the bot");
                } else {
                    frame.log(LogType.FATAL_ERR, "could not find file " + fileName + " and created token.txt: paste in your token and rerun the bot");
                }
            }
            token = Files.readString(Paths.get(fileName));
            if (token == null) frame.log(LogType.FATAL_ERR, "could not get token");
        } catch (IOException e) {
            frame.log(LogType.FATAL_ERR, "token.txt not found");
        }

        return token;
    }
}