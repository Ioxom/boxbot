package io.ioxcorp.ioxbot;

import io.ioxcorp.ioxbot.data.format.CustomUser;

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
                this.logInit("added icon to frame");
            } else {
                this.throwError("failed to add icon to frame; resources may be broken");
            }
        } catch (IOException e) {
            this.throwError("an IOException occurred while reading file \"images/box.png\"");
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
        this.logInit("added console to frame");

        //open the frame
        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize(new Dimension(600, 275));
        this.frame.setVisible(true);
        this.logInit("finished initializing frame");
    }

    //TODO: 0.2.0: make this all one method with type controlled by an enum or something

    //methods for logging
    public void logInit(String message) {
        this.console.append("\n[init] " + message);
    }

    public void logMain(String message) {
        this.console.append("\n[main] " + message);
    }

    public void logCommand(CustomUser user, String command, boolean containsUsed) {
        this.console.append("\n[cmd] " + user.getTag() + (containsUsed? " used " : " ")  + command);
    }

    public void throwError(String error, boolean fatal) {
        this.console.append((fatal?"\n[err/FATAL] " : "\n[err] ") + error + (fatal? "; closing ioxbox" : ""));
        if (fatal) {
            //wait for five seconds to allow for reading the error
            try {
                Thread.sleep(5000);
                System.exit(1);
            } catch (Exception e) {
                this.console.append("\n" + e.toString());
            }
        }
    }

    public void throwError(String error) {
        this.console.append("\n[err] " + error);
    }
}