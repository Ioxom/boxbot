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
import java.util.Objects;

public final class PromptTextField extends JTextField {
    private static final long serialVersionUID = -7065789752989953979L;
    private final String prompt;
    private String savedCommand;
    private boolean autofill;
    private final KeyAdapter autofillAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(final KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_TAB && getText().startsWith(IoxboxFrame.COMMAND_PREFIX) && savedCommand.startsWith(getText().split(" ")[0]) && autofill) {
                setText(savedCommand);
                drawText(" ".repeat(getText().length() * 3) + "press enter to run", getGraphics());
            }
        }
    };

    /**
     * constructs a new {@link PromptTextField} with autofill set to false
     * @param prompt the prompt to show in the text field, set to null or "" for no prompt
     */
    public PromptTextField(final String prompt) {
        this.autofill = false;
        this.prompt = Objects.requireNonNullElse(prompt, "");
        this.savedCommand = "/";
        //this gives autofill the ability to work by making sure that tab doesn't just leave the text field
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.emptySet());
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        //if the text in the field is empty draw the prompt
        if (getText().length() == 0) {
            this.drawText(this.prompt, g);
        //otherwise we try to get an autofill option
        } else if (autofill) {
            for (String command : IoxboxFrame.COMMANDS) {
                command = IoxboxFrame.COMMAND_PREFIX + command;
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

    /**
     * draws text to this field that is not selectable
     * adapted from: https://stackoverflow.com/a/24571681
     * @param text the text to be drawn
     * @param g the {@link Graphics} object used to draw the text
     */
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

    /**
     * enable or disable autofill
     * @param enabled whether to enable autofill or not
     */
    public void setAutofill(final boolean enabled) {
        if (enabled) {
            addAutofillEvent();
        } else {
            removeKeyListener(autofillAdapter);
        }
        autofill = enabled;
    }

    /**
     * adds the necessary {@link KeyEvent} to make autofill work: not calling this disables autofill
     */
    public void addAutofillEvent() {
        this.addKeyListener(autofillAdapter);
    }
}
