package io.ioxcorp.ioxbox;

import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.format.JacksonYeehawHelper;
import io.ioxcorp.ioxbox.listeners.Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

import static io.ioxcorp.ioxbox.Frame.LogType;

public class Main {

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
        } catch (Exception e) {
            //TODO: 0.2.0: replace printStackTrace() with an error thrown to frame console
            e.printStackTrace();
        }
    }

    public static Frame frame;
    static {
        //create frame
        frame = new Frame();
        frame.init();
    }

    public static HashMap<Long, Box> boxes;
    static {
        //read saved box data
        boxes = JacksonYeehawHelper.read();
    }

    public static void main(String[] args) {
        //throw error if version is not found
        //TODO: 0.2.0: explain why the version could not be obtained in error
        if (VERSION == null) {
            VERSION = "0.0.0";
            frame.log(Frame.LogType.ERROR, "could not get version; defaulting to 0.0.0");
        }

        //log in
        JDA api = null;
        try {
            String token = null;
            try {
                token = Files.readString(Paths.get("token.txt"));
                if (token == null) frame.log(LogType.FATAL_ERROR, "could not get token");
            //TODO: 0.2.0: find a way to prefer throwing FileNotFoundException over IOException as it's more informative
            } catch (FileNotFoundException e) {
                frame.log(LogType.FATAL_ERROR, "token.txt not found");
            } catch (IOException e) {
                frame.log(LogType.FATAL_ERROR, "an IOException occurred when reading file: token.txt");
            }
            api = JDABuilder.createDefault(token).build();
            Main.frame.log(LogType.INIT, "successfully logged in JDA");
        } catch (LoginException e) {
            frame.log(LogType.FATAL_ERROR, "invalid token");
        }

        //add event listeners
        if (api != null) {
            api.addEventListener(new Listener());
            frame.log(LogType.INIT, "initialized jda");
        } else {
            frame.log(LogType.ERROR, "failed to create JDA object for unknown reasons");
        }
    }
}
