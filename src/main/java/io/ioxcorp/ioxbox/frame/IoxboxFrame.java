package io.ioxcorp.ioxbox.frame;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.frame.logging.FileLogger;
import io.ioxcorp.ioxbox.frame.logging.LogHelper;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import javax.swing.ImageIcon;
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

import static io.ioxcorp.ioxbox.Main.FRAME;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * a bad swing gui
 * @author ioxom
 */
public final class IoxboxFrame {
    private final JTextArea console;
    private final JFrame jFrame;
    private final JPanel mainPanel;
    private final FileLogger logger;
    private final PromptTextField consoleInput;
    private final JButton reloadJDA;
    private final JButton clearConsole;
    private final JButton commandHelp;

    /**
     * creates a new frame with all variables set and the tile of "ioxbox v [version]".
     * this constructor should only be called once, as it opens the console with an [init] message and would be incorrect if called after initialisation.
     * it also adds the prompt "enter commands here" to the input console ({@link PromptTextField#PromptTextField(String)})<br>
     * note: this will create a new file due to the call of {@link FileLogger#FileLogger()}
     */
    public IoxboxFrame() {
        this.jFrame = new JFrame("ioxbox v " + Main.getVersion());
        this.console = new JTextArea("[init]: ioxbox v " + Main.getVersion() + " running on java " + System.getProperty("java.version") + LogHelper.getLogMessage(LogType.INIT, "loading ioxbox"));
        this.mainPanel = new JPanel(new BorderLayout(0, 0));
        this.logger = new FileLogger();
        this.consoleInput = new PromptTextField("enter commands here");
        this.reloadJDA = new JButton();
        this.commandHelp = new JButton();
        this.clearConsole = new JButton();
    }

    /**
     * initialises and opens the frame. this method is essentially "make the program available to the user", everything executed before it simply makes it seem like the program is loading slowly.
     */
    public void init() {
        this.logger.log(LogType.INIT, this.logger.getDebugInfo());

        this.jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //set icon
        this.jFrame.setIconImage(this.getImage("images/box.png"));
        //configure the console, adding a scroll bar and setting the colour
        final Dimension consoleSize = new Dimension(600, 300);
        this.console.setForeground(new Color(30, 30, 30));
        this.console.setFont(this.console.getFont().deriveFont(12.0F));
        this.console.setBackground(new Color(102, 102, 102));
        this.console.setSize(consoleSize);
        this.console.setEditable(false);
        this.console.setLineWrap(true);
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
        this.consoleInput.setAutofill(true);
        this.consoleInput.addActionListener(e -> {
            String message = this.consoleInput.getText();
            this.log(LogType.MAIN, message);
            this.consoleInput.clearText();
            this.handleCommands(message);
        });

        //add **buttons**
        final Color buttonColour = Color.DARK_GRAY;
        final Dimension buttonSize = new Dimension(50, 60);
        this.reloadJDA.setPreferredSize(buttonSize);
        this.reloadJDA.setBackground(buttonColour);
        this.setReloadJDAImage("images/lightning_bolt.png");
        this.commandHelp.setPreferredSize(buttonSize);
        this.commandHelp.setBackground(buttonColour);
        Image commandHelpImage = this.getImage("images/question_mark_icon.png");
        if (commandHelpImage == null) {
            this.log(LogType.ERR, "could not get resource for clear console button");
        } else {
            ImageIcon commandHelpIcon = new ImageIcon(commandHelpImage);
            this.commandHelp.setIcon(commandHelpIcon);
        }
        this.clearConsole.setPreferredSize(buttonSize);
        this.clearConsole.setBackground(buttonColour);
        Image clearConsoleImage = this.getImage("images/clear_console_icon.png");
        if (clearConsoleImage == null) {
            this.log(LogType.ERR, "could not get resource for jda clear console button");
        } else {
            ImageIcon clearConsoleIcon = new ImageIcon(clearConsoleImage);
            this.clearConsole.setIcon(clearConsoleIcon);
        }

        //add listeners to buttons
        this.clearConsole.addActionListener(e -> this.clearConsole());
        this.commandHelp.addActionListener(e -> this.log(LogType.HELP, "top button: reload jda\n"
                + "middle button: help\n"
                + "bottom button: clear console\n"
                + COMMAND_LIST
        ));
        this.reloadJDA.addActionListener(e -> {
            if (Main.isFullyConnected()) {
                Main.shutdown();
            } else {
                Main.connect();
                FRAME.log(LogType.MAIN, "reconnected JDA");
            }
        });

        //ensure that autofill works for consoleInput
        this.consoleInput.addAutofillEvent();

        //add everything to main panel, utilising another panel to get the buttons in a line
        JPanel panel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.consoleInput, BorderLayout.SOUTH);
        panel.add(this.reloadJDA, BorderLayout.NORTH);
        panel.add(this.commandHelp, BorderLayout.CENTER);
        panel.add(this.clearConsole, BorderLayout.SOUTH);
        this.mainPanel.add(panel, BorderLayout.EAST);

        //open the frame
        this.jFrame.setSize(consoleSize);
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
            if (!Main.getConfig().logCommands()) {
                return;
            }

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
            "/ping",
            "/exit",
            "/configure"
    };

    public static final String COMMAND_LIST = "=== command list start ===\n"
            + COMMANDS[0] + ": display this list\n"
            + COMMANDS[1] + ": clear the console\n"
            + COMMANDS[2] + ": reload JDA, disconnecting and reconnecting to discord\n"
            + COMMANDS[3] + ": disconnect from discord, if already disconnected does nothing\n"
            + COMMANDS[4] + ": connect to discord, if already connected reloads jda\n"
            + COMMANDS[5] + ": get the current ping\n"
            + COMMANDS[6] + ": disconnects from discord and ends the process\n"
            + COMMANDS[7] + ": rereads the config file\n"
            + "=== command list end ===";

    public void handleCommands(final String command) {
        if (!command.startsWith("/")) {
            this.log(LogType.MAIN, "invalid command: commands must start with /\nuse /commands for a list");
            return;
        }

        for (int i = 0; i < COMMANDS.length; i++) {
            if (COMMANDS[i].equals(command)) {
                switch (i) {
                    case 0:
                        this.log(LogType.HELP, COMMAND_LIST);
                        return;
                    case 1:
                        this.clearConsole();
                        return;
                    case 2:
                        Main.reloadJDA();
                        return;
                    case 3:
                        if (Main.isFullyConnected()) {
                            Main.shutdown();
                        } else {
                            this.log(LogType.ERR, "could not disconnect from discord: already disconnected");
                        }
                        return;
                    case 4:
                        if (Main.isFullyConnected()) {
                            this.log(LogType.MAIN, "already connected; reloading JDA");
                        } else {
                            Main.connect();
                            this.log(LogType.MAIN, "reconnected to discord");
                        }
                        return;
                    case 5:
                        if (Main.isFullyConnected()) {
                            final long time = System.currentTimeMillis();
                            Objects.requireNonNull(Objects.requireNonNull(Main.getApi().getGuildById(Main.getConfig().getMainServer())).getTextChannelById(Main.getConfig().getSpamChannel())).sendMessage("calculating ping").queue(message -> {
                                long ping = System.currentTimeMillis() - time;
                                this.log(LogType.MAIN, "current ping: " + ping + "ms");
                                message.editMessage("ping is " + ping + "ms").queue();
                            });
                        } else {
                            this.log(LogType.ERR, "not fully connected to JDA, could not get ping");
                        }
                        return;
                    case 6:
                        System.exit(0);
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

        this.log(LogType.MAIN, "command not found");
    }

    private void clearConsole() {
        this.console.setText(LogType.MAIN.getValue() + ": ioxbox v " + Main.getVersion() + " running on java " + System.getProperty("java.version"));
    }

    private Image getImage(final String path) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream != null) {
                return ImageIO.read(inputStream);
            } else {
                this.log(LogType.ERR, "failed to get image: " + path + "; resources may be broken");
                return null;
            }
        } catch (IOException e) {
            this.log(LogType.FATAL_ERR, "an IOException occurred while reading file \"images/box.png\"");
            return null;
        }
    }

    public void setReloadJDAImage(final String path) {
        Image reloadJDAImage = this.getImage(path);
        if (reloadJDAImage == null) {
            this.log(LogType.ERR, "could not get resource for reload JDA button");
        } else {
            ImageIcon reloadJDAIcon = new ImageIcon(reloadJDAImage);
            this.reloadJDA.setIcon(reloadJDAIcon);
        }
    }
}
