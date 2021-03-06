package io.ioxcorp.ioxbox.data.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.frame.logging.LogType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Config {
    @JsonIgnore
    private boolean isFirstRun;
    @JsonProperty("admins")
    private ArrayList<Long> admins;
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
    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @JsonCreator
    public Config() {
        this.isFirstRun = true;
    }

    public Config(final ArrayList<Long> admins, final long mainServer, final long spamChannel, final String token, final String prefix, final boolean logCommands) {
        this.isFirstRun = false;
        this.admins = admins;
        this.mainServer = mainServer;
        this.spamChannel = spamChannel;
        this.token = token;
        this.prefix = prefix;
        this.logCommands = logCommands;
    }

    public void readConfig() {

        //create file if it doesn't exist
        File file = new File("config.json");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    FileWriter writer = new FileWriter(file);
                    writer.write(MAPPER.writeValueAsString(new Config(new ArrayList<>(), 0L, 0L, "null", "null", true)));
                    writer.close();
                    Main.FRAME.log(LogType.FATAL_ERR, "no config file found, created one");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //read config
        Config readConfig = null;
        try {
            readConfig = MAPPER.readValue(file, Config.class);
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

        setThis(readConfig);

        this.isFirstRun = false;
    }

    private void setThis(final Config config) {
        this.isFirstRun = config.isFirstRun;
        this.admins = config.getAdmins();
        this.mainServer = config.mainServer;
        this.spamChannel = config.spamChannel;
        this.token = config.token;
        this.prefix = config.prefix;
        this.logCommands = config.logCommands;
    }

    @JsonGetter
    public ArrayList<Long> getAdmins() {
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
}
