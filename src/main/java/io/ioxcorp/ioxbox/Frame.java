package io.ioxcorp.ioxbox;

import io.ioxcorp.ioxbox.data.format.CustomUser;

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

public class Frame {
    private final JTextArea console;
    private final JFrame frame;
    private final JPanel panel;
    public Frame() {
        this.frame = new JFrame("ioxbox v " + Main.VERSION);
        this.console = new JTextArea("[init] loading ioxbox");
        this.panel = new JPanel(new BorderLayout());
    }

    public void init() {
        //set icon
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/box.png");
            if (inputStream != null) {
                Image image = ImageIO.read(inputStream);
                this.frame.setIconImage(image);
                this.log(LogType.INIT, "added icon to frame");
            } else {
                this.log(LogType.ERROR, "failed to add icon to frame; resources may be broken");
            }
        } catch (IOException e) {
            this.log(LogType.FATAL_ERROR, "an IOException occurred while reading file \"images/box.png\"");
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
        this.frame.setContentPane(this.panel);
        this.log(LogType.INIT, "added console to frame");

        //open the frame
        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize(new Dimension(600, 275));
        this.frame.setVisible(true);
        this.log(LogType.INIT, "finished initialising frame");
    }

    //TODO: 0.3.0: make this better

    public void logCommand(CustomUser user, String command, boolean containsUsed) {
        this.console.append("\n[cmd] " + user.getTag() + (containsUsed? " used " : " ")  + command);
    }

    public enum LogType {
        MAIN,
        INIT,
        ERROR,
        FATAL_ERROR
    }

    public void log(LogType type, String message) {
        switch (type) {
            case INIT:
                this.console.append("\n[init] " + message);
                break;
            case MAIN:
                this.console.append("\n[main] " + message);
                break;
            case ERROR:
                this.console.append("\n[err] " + message);
                break;
            case FATAL_ERROR:
                this.console.append("\n[err/FATAL] " + message + "; closing ioxbox");
                //wait for five seconds to allow for reading the error
                try {
                    Thread.sleep(5000);
                    System.exit(1);
                } catch (Exception e) {
                    System.exit(1);
                }
        }
    }
}