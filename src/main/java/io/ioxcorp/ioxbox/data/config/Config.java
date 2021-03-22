package io.ioxcorp.ioxbox.data.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.frame.logging.LogType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class Config {
    @JsonIgnore
    private boolean isFirstRun;
    @JsonProperty("admins")
    private List<Long> admins;
    @JsonProperty("mainServer")
    private long mainServer;
    @JsonProperty("spamChannel")
    private long spamChannel;
    @JsonProperty("token")
    private String token;
    @JsonProperty("prefix")
    private String prefix;
    @JsonProperty("logCommands")
    private boolean logCommands;

    @JsonIgnore
    private static final File CONFIG_FILE = new File("config.json");

    @JsonCreator
    public Config() {
        this.isFirstRun = true;
    }

    /**
     * creates a new config with all values set to the parameters
     * @param admins an {@link ArrayList} of user ids: the admins who are allowed to use config commands from discord
     * @param mainServer the id of the server to find the {@link Config#spamChannel} in
     * @param spamChannel the id of the channel to use when getting ping from the console
     * @param token the bot's token
     * @param prefix the prefix to be used before bot commands
     * @param logCommands whether or not to log command usages
     */
    public Config(final List<Long> admins, final long mainServer, final long spamChannel, final String token, final String prefix, final boolean logCommands) {
        this.isFirstRun = false;
        this.admins = admins;
        this.mainServer = mainServer;
        this.spamChannel = spamChannel;
        this.token = token;
        this.prefix = prefix;
        this.logCommands = logCommands;
    }

    /**
     * sets all values in the config to what's found in config.json ({@link Config#CONFIG_FILE})
     */
    public void readConfig() {
        //create file if it doesn't exist
        if (!CONFIG_FILE.exists()) {
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                if (CONFIG_FILE.createNewFile()) {
                    String tokenToMigrate = getTokenFromDedicatedFile();
                    writer.write(Main.MAPPER.writeValueAsString(new Config(new ArrayList<>(), 0L, 0L, tokenToMigrate, "null", true)));
                    Main.FRAME.log(LogType.ERR, "no config file found, created one\nyou can fill this in with the required information");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //read config
        Config readConfig = null;
        try {
            readConfig = Main.MAPPER.readValue(CONFIG_FILE, Config.class);
        } catch (JsonMappingException e) {
            if (isFirstRun) {
                Main.FRAME.log(LogType.FATAL_ERR, "failed to map json of config: " + e);
                return;
            } else {
                Main.FRAME.log(LogType.ERR, "failed to map json of config: " + e + ";using old configuration");
            }
        } catch (JsonParseException e) {
            if (isFirstRun) {
                Main.FRAME.log(LogType.FATAL_ERR, "failed to parse json of config: " + e);
                return;
            } else {
                Main.FRAME.log(LogType.ERR, "failed to parse json of config: " + e + ";using old configuration");
            }
        } catch (IOException e) {
            if (isFirstRun) {
                Main.FRAME.log(LogType.FATAL_ERR, "an IOException occurred while reading config: " + e);
                return;
            } else {
                Main.FRAME.log(LogType.ERR, "an IOException occurred while reading config: " + e + ";using old configuration");
            }
        }

        if (readConfig != null) {
            Main.FRAME.log(this.isFirstRun ? LogType.INIT : LogType.MAIN, "successfully " + (this.isFirstRun ? "read" : "reread") + " config file");
        } else if (!this.isFirstRun) {
            Main.FRAME.log(LogType.ERR, "failed to read config due to an unknown error, using previously read version");
            return;
        } else {
            Main.FRAME.log(LogType.FATAL_ERR, "could not read config due to an unknown error");
            return;
        }

        this.setThis(readConfig);

        this.isFirstRun = false;
    }

    /**
     * set all values of this object to the config passed in
     * @param config the config to pull values from
     */
    private void setThis(final Config config) {
        isFirstRun = config.isFirstRun;
        admins = config.getAdmins();
        mainServer = config.mainServer;
        spamChannel = config.spamChannel;
        token = config.token;
        prefix = config.prefix;
        logCommands = config.logCommands;
    }

    @Override
    public String toString() {
        final StringBuilder adminsAsString = new StringBuilder();
        for (int i = 0; i < this.admins.size(); i++) {
            final long adminId = this.admins.get(i);
            if (i == this.admins.size() - 1) {
                adminsAsString.append(", ").append(adminId).append("\n");
            } else if (i == 0) {
                adminsAsString.append("admins: ").append(adminId);
            } else {
                adminsAsString.append(", ").append(adminId);
            }
        }

        return adminsAsString
                + "main server id: " + mainServer + "\n"
                + "spam channel id: " + spamChannel + "\n"
                + "token: " + token + "\n"
                + "prefix: " + prefix + "\n"
                + "log commands? " + logCommands;
    }

    /**
     * we used to store the token in a dedicated file named "token.txt", we use this method to migrate it<br>
     * deletes token.txt after getting contents.
     * @return the contents of token.txt or "null" if it isn't found
     */
    private static String getTokenFromDedicatedFile() {
        Path path = Paths.get("token.txt");
        boolean found = path.toFile().exists();
        if (!found) {
            return "null";
        } else {
            try {
                return Files.readString(path);
            //this should never be thrown as we've already checked if the file exists
            } catch (IOException e) {
                return "null";
            } catch (OutOfMemoryError e) {
                Main.FRAME.log(LogType.ERR, "token.txt was too large to read, aborting and writing \"null\" to config.json");
                return "null";
            } finally {
                try {
                    Files.delete(path);
                } catch (IOException ignored) {
                    //this is ignored because it should be impossible to throw
                }
            }
        }
    }

    @JsonGetter
    public List<Long> getAdmins() {
        return this.admins;
    }

    @JsonGetter
    public long getMainServer() {
        return this.mainServer;
    }

    @JsonGetter
    public long getSpamChannel() {
        return this.spamChannel;
    }

    @JsonGetter
    public String getToken() {
        return this.token;
    }

    @JsonGetter
    public String getPrefix() {
        return this.prefix;
    }

    @JsonGetter("logCommands")
    public boolean logCommands() {
        return this.logCommands;
    }

    @JsonSetter
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    @JsonSetter("logCommands")
    public void setCommandLogging(final boolean log) {
        this.logCommands = log;
    }
}
