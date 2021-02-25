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


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * a bad swing gui
 * @author ioxom
 */
//TODO: 1.0.0: (thonkman) make this look good, maybe move away from swing to javaFX
public final class Frame {
    private final JTextArea console;
    private final JFrame jFrame;
    private final JPanel mainPanel;
    private final FileLogger logger;
    private final PromptTextField consoleInput;
    private final JButton reloadJDA;
    private final JButton clearConsole;
    private final JButton commandHelp;
    private boolean connect;

    public Frame() {
        this.jFrame = new JFrame("ioxbox v " + Main.getVersion());
        this.console = new JTextArea("[init]: ioxbox v " + Main.getVersion() + " running on java " + System.getProperty("java.version") + "\n[init] loading ioxbox");
        this.mainPanel = new JPanel(new BorderLayout(0, 0));
        this.logger = new FileLogger();
        this.consoleInput = new PromptTextField("prompt");
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
//                this.log(LogType.INIT, "added icon to frame");
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
            //TODO: handle commands here
            String message = this.consoleInput.getText();
            Main.FRAME.log(LogType.MAIN, message);
            this.consoleInput.clearText();
        });

        //add **buttons**
        //TODO: icons
        this.reloadJDA.setPreferredSize(new Dimension(50, 50));
        this.reloadJDA.setBackground(Color.DARK_GRAY);
        this.commandHelp.setPreferredSize(new Dimension(50, 50));
        this.commandHelp.setBackground(Color.DARK_GRAY);
        this.clearConsole.setPreferredSize(new Dimension(50, 50));
        this.clearConsole.setBackground(Color.DARK_GRAY);

        //add listeners to buttons
        this.clearConsole.addActionListener(e -> this.console.setText("[main]: ioxbox v " + Main.getVersion() + " running on java " + System.getProperty("java.version")));
        this.commandHelp.addActionListener(e -> this.log(LogType.HELP, "top button: reload jda\nmiddle button: help\nbottom button: clear console"));
        this.connect = false;
        this.reloadJDA.addActionListener(e -> {
            if (connect) {
                Main.connectJDA();
                Main.addListeners();
                Main.FRAME.log(LogType.MAIN, "reconnected JDA");
                this.connect = false;
            } else {
                Main.shutdownJDA();
                Main.FRAME.log(LogType.MAIN, "disconnected JDA");
                this.connect = true;
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

            String logMessage = LogHelper.getLogMessage(LogType.CMD, user.getTag() + " used " + message);
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
}
