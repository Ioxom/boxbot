package io.ioxcorp.ioxbox.frame;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.security.auth.login.LoginException;

import static io.ioxcorp.ioxbox.Main.FRAME;

/**
 * system for handling console commands
 * @author ioxom
 */
public final class FrameCommandSystem {
    private FrameCommandSystem() {

    }

    public static final String COMMAND_PREFIX = "/";

    public static final String[] COMMANDS = {
            "commands",
            "clear",
            "reload",
            "disconnect",
            "connect",
            "ping",
            "exit",
            "configure"
    };

    /**
     * @param index the index of the command in {@link FrameCommandSystem#COMMANDS}
     * @return the commands with added prefix
     */
    private static String getCommand(final int index) {
        return COMMAND_PREFIX + COMMANDS[index];
    }

    public static final String COMMAND_LIST = "=== command list start ===\n"
            + getCommand(0) + ": display FRAME list\n"
            + getCommand(1) + ": clear the console\n"
            + getCommand(2) + ": reload JDA, disconnecting and reconnecting to discord\n"
            + getCommand(3) + ": disconnect from discord, if already disconnected does nothing\n"
            + getCommand(4) + ": connect to discord, if already connected reloads jda\n"
            + getCommand(5) + ": get the current ping\n"
            + getCommand(6) + ": disconnects from discord and ends the process\n"
            + getCommand(7) + ": rereads the config file\n"
            + "=== command list end ===";

    /**
     * handle the sent command, running the associated command or printing "command not found"
     * @param command the command to run
     */
    //for some reason these switch numbers are considered magic
    @SuppressWarnings("checkstyle:MagicNumber")
    public static void handleCommands(final String command) {
        if (command.startsWith(COMMAND_PREFIX)) {
            for (int i = 0; i < COMMANDS.length; i++) {
                if ((getCommand(i)).equals(command)) {
                    switch (i) {
                        case 0:
                            FRAME.log(LogType.HELP, COMMAND_LIST);
                            return;
                        case 1:
                            FRAME.clearConsole();
                            return;
                        case 2:
                            Main.reloadJDA();
                            return;
                        case 3:
                            if (Main.isFullyConnected()) {
                                Main.shutdown();
                            } else {
                                FRAME.log(LogType.ERR, "could not disconnect from discord: already disconnected");
                            }
                            return;
                        case 4:
                            if (Main.isFullyConnected()) {
                                FRAME.log(LogType.MAIN, "already connected; reloading JDA");
                            } else {
                                Main.connect();
                                FRAME.log(LogType.MAIN, "reconnected to discord");
                            }
                            return;
                        case 5:
                            if (Main.isFullyConnected()) {
                                final long time = System.currentTimeMillis();
                                final Guild guild = Main.getApi().getGuildById(Main.getConfig().getMainServer());
                                if (guild == null) {
                                    FRAME.log(LogType.ERR, "guild not found; could not get ping\nplease fix your config!");
                                    return;
                                }
                                final MessageChannel channel = guild.getTextChannelById(Main.getConfig().getSpamChannel());
                                if (channel == null) {
                                    FRAME.log(LogType.ERR, "channel not found; could not get ping\nplease fix your config!");
                                    return;
                                }
                                channel.sendMessage("calculating ping").queue(message -> {
                                    long ping = System.currentTimeMillis() - time;
                                    FRAME.log(LogType.MAIN, "current ping: " + ping + "ms");
                                    message.editMessage("ping is " + ping + "ms").queue();
                                });
                            } else {
                                FRAME.log(LogType.ERR, "not fully connected to JDA, could not get ping");
                            }
                            return;
                        case 6:
                            Main.exit(0);
                            return;
                        case 7:
                            final String oldToken = Main.getConfig().getToken();
                            Main.getConfig().readConfig();
                            final String token = Main.getConfig().getToken();
                            if (!oldToken.equals(token)) {
                                FRAME.log(LogType.MAIN, "got different token; reloading JDA");
                                Main.shutdown();
                                try {
                                    Main.setApi(JDABuilder.createDefault(token));
                                    Main.addListeners();
                                } catch (LoginException e) {
                                    FRAME.log(LogType.ERR, "got invalid token on config reload, enter a working one and do /configure again");
                                }
                            }
                            return;
                    }
                }
            }

            //if we arrive here the switch was never triggered and therefore no commands were found
            FRAME.log(LogType.MAIN, "command not found");
        } else {
            FRAME.log(LogType.ERR, "invalid command: commands must start with " + COMMAND_PREFIX + "\nuse " + COMMAND_PREFIX + COMMANDS[0] + " for a list");
        }
    }
}
