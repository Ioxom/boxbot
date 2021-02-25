package io.ioxcorp.ioxbox;

import io.ioxcorp.ioxbox.data.format.Box;
import io.ioxcorp.ioxbox.data.json.JacksonYeehawHelper;
import io.ioxcorp.ioxbox.frame.Frame;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;
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
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public final class Main extends ListenerAdapter {
    private Main() {

    }

    public static final Random RANDOM = new Random();

    //get the version from a .properties that's saved in the .jar that shadowJar produces
    private static String version;
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

            version = properties.getProperty("version");
            properties.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getVersion() {
        return Main.version;
    }

    public static final Frame FRAME = new Frame();
    static {
        FRAME.init();
    }

    public static final HashMap<Long, Box> BOXES;
    static {
        //read saved box data
        BOXES = JacksonYeehawHelper.read();
    }

    private static final String TOKEN = getToken();
    private static JDA api;

    public static void main(final String[] args) {

        //throw error if version is not found
        if (version == null) {
            version = "0.0.0";
            FRAME.log(LogType.ERR, "could not get version from \"ioxbox.properties\". this file should normally be stored in the .jar file that is run, but it seems an error occurred on saving.");
        }

        //log in
        connectJDA();

        if (api != null) {
            addListeners();
            FRAME.log(LogType.INIT, "initialized jda");
        } else {
            FRAME.log(LogType.ERR, "failed to create JDA object for unknown reasons");
        }
    }

    private static String getToken() {
        String token = null;
        String fileName = "token.txt";
        try {
            if (!Files.exists(Paths.get(fileName))) {
                boolean created = new File(fileName).createNewFile();
                if (created) {
                    FRAME.log(LogType.FATAL_ERR, "could not find token file and created token.txt: paste in your token and rerun the bot");
                } else {
                    FRAME.log(LogType.FATAL_ERR, "could not find file " + fileName + " and created token.txt: paste in your token and rerun the bot");
                }
            }
            token = Files.readString(Paths.get(fileName));
            if (token == null) {
                FRAME.log(LogType.FATAL_ERR, "could not get token");
            }
        } catch (IOException e) {
            FRAME.log(LogType.FATAL_ERR, "token.txt not found");
        }

        return token;
    }

    public static void reloadJDA() {
        shutdownJDA();
        connectJDA();
        addListeners();
        FRAME.log(LogType.INIT, "reconnected jda");
    }

    public static void shutdownJDA() {
        for (ConfirmationGetter confirmationGetter : ConfirmationGetter.CONFIRMATION_GETTERS.values()) {
            confirmationGetter.getChannel().sendMessage(EmbedHelper.simpleErrorEmbed(confirmationGetter.getId(), "confirmation getter closed due to JDA shutdown. ask again once this bot is back online!")).queue();
            ConfirmationGetter.CONFIRMATION_GETTERS.remove(confirmationGetter.getId());
        }
        api.shutdown();
        FRAME.log(LogType.MAIN, "shut down JDA, disconnected from discord");
    }

    public static void connectJDA() {
        try {
            api = JDABuilder.createDefault(TOKEN).build();
            Main.FRAME.log(LogType.INIT, "successfully logged in JDA");
        } catch (LoginException e) {
            FRAME.log(LogType.FATAL_ERR, "invalid token");
        }
    }

    public static void addListeners() {
        api.addEventListener(new MainListener(), new ConfirmationGetterListener(), new StatusSetter(20), new Main());
    }

    @Override
    public void onReady(final @NotNull ReadyEvent e) {
        FRAME.log(LogType.MAIN, "fully loaded JDA; connected to discord");
    }
}
