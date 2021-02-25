package io.ioxcorp.ioxbox.frame;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.frame.logging.FileLogger;
import io.ioxcorp.ioxbox.frame.logging.LogHelper;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static io.ioxcorp.ioxbox.Main.FRAME;

/**
 * a bad swing gui
 * @author ioxom
 */
public final class Frame {
    private final JTextArea console;
    private final JFrame jFrame;
    private final JPanel mainPanel;
    private final FileLogger logger;
    private final PromptTextField consoleInput;
    private final JButton reloadJDA;
    private final JButton clearConsole;
    private final JButton commandHelp;
    private boolean connected;

    public Frame() {
        this.jFrame = new JFrame("ioxbox v " + Main.getVersion());
        this.console = new JTextArea("[init]: ioxbox v " + Main.getVersion() + " running on java " + System.getProperty("java.version") + "\n[init] loading ioxbox");
        this.mainPanel = new JPanel(new BorderLayout(0, 0));
        this.logger = new FileLogger();
        this.consoleInput = new PromptTextField("enter commands here");
        this.reloadJDA = new JButton();
        this.commandHelp = new JButton();
        this.clearConsole = new JButton();
    }

    public void init() {
        this.logger.log(LogType.INIT, this.logger.getDebugInfo());

        this.jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //set icon
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/box.png");
            if (inputStream != null) {
                Image image = ImageIO.read(inputStream);
                this.jFrame.setIconImage(image);
                this.log(LogType.INIT, "added icon to frame");
            } else {
                this.log(LogType.ERR, "failed to add icon to frame; resources may be broken");
            }
        } catch (IOException e) {
            this.log(LogType.FATAL_ERR, "an IOException occurred while reading file \"images/box.png\"");
        }
        //configure the console, adding a scroll bar and setting the colour
        final Dimension consoleSize = new Dimension(600, 375);
        this.console.setBackground(Color.GRAY);
        this.console.setSize(consoleSize);
        this.console.setEditable(false);
        //create a pane to allow the console to have scrolling
        JScrollPane pane = new JScrollPane(this.console);
        pane.setPreferredSize(consoleSize);
        //configure the main background panel
        this.mainPanel.setPreferredSize(consoleSize);
        this.mainPanel.add(pane);
        this.jFrame.setContentPane(this.mainPanel);

        //yes
        //input console
        Dimension consoleInputSize = new Dimension(600, 20);
        this.consoleInput.setPreferredSize(consoleInputSize);
        this.consoleInput.setEditable(true);
        this.consoleInput.addActionListener(e -> {
            String message = this.consoleInput.getText();
            FRAME.log(LogType.MAIN, message);
            this.consoleInput.clearText();
            this.handleCommands(message);
        });

        //add **buttons**
        //TODO: icons - note that jda connection button icon should differ depending on whether we're connected or not
        this.reloadJDA.setPreferredSize(new Dimension(50, 50));
        this.reloadJDA.setBackground(Color.DARK_GRAY);
        this.commandHelp.setPreferredSize(new Dimension(50, 50));
        this.commandHelp.setBackground(Color.DARK_GRAY);
        this.clearConsole.setPreferredSize(new Dimension(50, 50));
        this.clearConsole.setBackground(Color.DARK_GRAY);

        //add listeners to buttons
        this.clearConsole.addActionListener(e -> this.clearConsole());
        this.commandHelp.addActionListener(e -> this.log(LogType.HELP, "top button: reload jda\nmiddle button: help\nbottom button: clear console"));
        this.connected = true;
        this.reloadJDA.addActionListener(e -> {
            if (connected) {
                Main.shutdownJDA();
                this.connected = false;
            } else {
                Main.connectJDA();
                Main.addListeners();
                FRAME.log(LogType.MAIN, "reconnected JDA");
                this.connected = true;
            }
        });

        //add everything to main panel, utilising another panel to get the buttons in a line
        JPanel panel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.consoleInput, BorderLayout.SOUTH);
        panel.add(this.reloadJDA, BorderLayout.NORTH);
        panel.add(this.commandHelp, BorderLayout.CENTER);
        panel.add(this.clearConsole, BorderLayout.SOUTH);
        this.mainPanel.add(panel, BorderLayout.EAST);

        //open the frame
        this.jFrame.setSize(new Dimension(600, 275));
        this.jFrame.setVisible(true);
    }

    public void log(final LogType type, final String message) {
        if (type == LogType.CMD) {
            throw new IllegalArgumentException("cannot execute case of CMD without author information, use Frame#log(LogType, String, Object)");
        } else {
            LogHelper.handleNormalLogs(LogHelper.LoggerType.CONSOLE, type, message);
        }
    }

    public void log(final LogType type, final String message, final Object author) {
        if (type == LogType.CMD) {
            CustomUser user;
            if (author instanceof User) {
                user = new CustomUser((User) author);
            } else if (author instanceof CustomUser) {
                user = (CustomUser) author;
            } else {
                throw new IllegalArgumentException("object \"author\" passed to Frame#log(LogType type, String message, Object author) must be a User or CustomUser");
            }

            String logMessage = LogHelper.getLogMessage(LogType.CMD, user.getAsTag() + " used " + message);
            this.console.append(logMessage);
        } else {
            LogHelper.handleNormalLogs(LogHelper.LoggerType.CONSOLE, type, message);
        }
    }

    public FileLogger getFileLogger() {
        return this.logger;
    }

    public JTextArea getConsole() {
        return this.console;
    }

    public static final String[] COMMANDS = {
            "/commands",
            "/clear",
            "/reload",
            "/disconnect",
            "/connect",
            "/ping"
    };

    public void handleCommands(final String command) {
        if (!command.startsWith("/")) {
            FRAME.log(LogType.MAIN, "invalid command: commands must start with /\nuse /commands for a list");
            return;
        }

        for (int i = 0; i < COMMANDS.length; i++) {
            if (COMMANDS[i].equals(command)) {
                switch (i) {
                    case 0:
                        FRAME.log(LogType.MAIN, "command list");
                        return;
                    case 1:
                        FRAME.clearConsole();
                        return;
                    case 2:
                        Main.reloadJDA();
                        return;
                    case 3:
                        Main.shutdownJDA();
                        return;
                    case 4:
                        Main.connectJDA();
                        FRAME.log(LogType.MAIN, "reconnected to discord");
                        return;
                    case 5:
                        if (Main.isFullyConnected()) {
                            final long time = System.currentTimeMillis();
                            Objects.requireNonNull(Objects.requireNonNull(Main.getApi().getGuildById(618926084326686723L)).getTextChannelById(784440682436886588L)).sendMessage("calculating ping").queue(message -> {
                                long ping = System.currentTimeMillis() - time;
                                FRAME.log(LogType.MAIN, "current ping: " + ping + "ms");
                                message.editMessage("ping is " + ping + "ms").queue();
                            });
                        } else {
                            FRAME.log(LogType.ERR, "not fully connected to JDA, could not get ping");
                        }
                        return;
                }
            }
        }

        FRAME.log(LogType.MAIN, "command not found");
    }

    public void clearConsole() {
        this.console.setText("[main]: ioxbox v " + Main.getVersion() + " running on java " + System.getProperty("java.version"));
    }
}
