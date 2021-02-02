package ca.ioxom.boxbot;

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

public class Main {

    //get the version from a .properties that's saved in the .jar that gradle produces
    public static String VERSION;
    static {
        try {
            Properties properties = new Properties();
            String fileName = "boxbot.properties";
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
            e.printStackTrace();
        }
    }

    public static Frame frame;
    public static HashMap<Long, Box> boxes = new HashMap<>();

    public static void main(String[] args) {

        //create frame
        frame = new Frame();
        frame.init();

        //read saved box data
        HashMap<Long, Box> yes = new HashMap<>();
        yes.put(new CustomUser(6L, "h", 5).id, new Box(new CustomUser(6L, "h", 5), "h"));

        JacksonYeehawHelper.save(yes);

        boxes = JacksonYeehawHelper.read();

        //throw error if version is not found
        if (VERSION == null) {
            VERSION = "0.0.0";
            frame.throwError("could not get version; defaulting to 0.0.0");
        }

        //log in
        JDA api = null;
        try {
            String token = null;
            try {
                token = Files.readString(Paths.get("token.txt"));
                if (token == null) frame.throwError("could not get token", true);
            } catch (FileNotFoundException e) {
                frame.throwError("token.txt not found", true);
            } catch (IOException e) {
                frame.throwError("an IOException occurred when reading file: token.txt", true);
            }
            api = JDABuilder.createDefault(token).build();
            Main.frame.logInit("successfully logged in JDA");
        } catch (LoginException e) {
            frame.throwError("invalid token", true);
        }

        //add event listeners
        if (api != null) {
            api.addEventListener(new Listener());
            frame.logInit("initialized jda");
        } else {
            frame.throwError("failed to create JDA object", true);
        }
    }
}
