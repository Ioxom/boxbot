package io.ioxcorp.ioxbox.frame;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;

public final class PromptTextField extends JTextField {
    private final String prompt;
    private String savedCommand;

    public PromptTextField(final String prompt) {
        this.prompt = prompt;
        this.savedCommand = "/";
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.emptySet());
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        //if the text in the field is empty draw the prompt
        if (getText().length() == 0) {
            this.drawText(this.prompt, g);
        //otherwise we try to get an autofill option
        } else {
            for (String command : IoxboxFrame.COMMANDS) {
                if (command.startsWith(this.getText()) && !command.equals(this.getText())) {
                    this.savedCommand = command;
                    drawText(" ".repeat(this.getText().length() * 3) + command + " (press tab)", g);
                    return;
                } else if (command.equals(this.getText())) {
                    this.savedCommand = command;
                    this.drawText(" ".repeat(this.getText().length() * 3) + "press enter to run", g);
                    return;
                }
            }
        }
    }

    public void clearText() {
        this.setText("");
    }

    private void drawText(final String text, final Graphics g) {
        int h = getHeight();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Insets ins = getInsets();
        FontMetrics fm = g.getFontMetrics();
        int c0 = getBackground().getRGB();
        int c1 = getForeground().getRGB();
        int m = 0xfefefefe;
        int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
        g.setColor(new Color(c2, true));
        g.drawString(text, ins.left, h / 2 + fm.getAscent() / 2 - 1);
    }

    public void addAutofillEvent() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_TAB && getText().startsWith("/") && savedCommand.startsWith(getText().split(" ")[0])) {
                    setText(savedCommand);
                    drawText(" ".repeat(getText().length() * 3) + "press enter to run", getGraphics());
                }
            }
        });
    }
}
