package io.ioxcorp.ioxbox.frame;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.frame.logging.FileLogger;
import io.ioxcorp.ioxbox.frame.logging.LogHelper;
import io.ioxcorp.ioxbox.frame.logging.LogType;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
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
public class Frame {
    private final JTextArea console;
    private final JFrame jFrame;
    private final JPanel panel;
    private final FileLogger logger;

    public Frame() {
        this.jFrame = new JFrame("ioxbox v " + Main.VERSION);
        this.console = new JTextArea("[init] loading ioxbox");
        this.panel = new JPanel(new BorderLayout());
        this.logger = new FileLogger();
    }

    public void init() {
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
        //we also disallow editing for the console because yeah obvious
        final Dimension consoleSize = new Dimension(500, 375);
        this.console.setBackground(Color.GRAY);
        this.console.setSize(consoleSize);
        this.console.setEditable(false);

        //create a pane to allow the console to have scrolling
        JScrollPane pane = new JScrollPane(this.console);
        pane.setPreferredSize(consoleSize);

        //configure the main background panel
        this.panel.setPreferredSize(consoleSize);
        this.panel.add(pane);
        this.jFrame.setContentPane(this.panel);
        this.log(LogType.INIT, "added console to frame");

        //open the frame
        this.jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.jFrame.setSize(new Dimension(600, 275));
        this.jFrame.setVisible(true);
        this.log(LogType.INIT, "finished initialising frame");


    }

    public void log(LogType type, String message) {
        if (type == LogType.CMD) {
            throw new IllegalArgumentException("cannot execute case of CMD without author information, use Frame#log(LogType, String, Object)");
        } else {
            LogHelper.handleNormalLogs(LogHelper.LoggerType.CONSOLE, type, message);
        }
    }

    public void log(LogType type, String message, Object author) {
        if (type == LogType.CMD) {
            if (author instanceof User) {
                author = new CustomUser((User) author);
            }

            if (author instanceof CustomUser) {
                message = LogHelper.replaceNewlines(type, message);
                this.console.append("\n[cmd]: " + ((CustomUser) author).getTag() + " used " + message);
                this.logger.log(type, message, author);
            } else {
                throw new IllegalArgumentException("object \"author\" passed to Frame#log(LogType type, String message, Object author) must be a User or CustomUser");
            }
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