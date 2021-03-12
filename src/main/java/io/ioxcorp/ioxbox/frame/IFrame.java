package io.ioxcorp.ioxbox.frame;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.frame.logging.LogType;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * custom frame used to ensure proper shutdown of {@link Main#api}
 * @author ioxom
 */
public final class IFrame extends JFrame implements WindowListener {

    public IFrame(final String name) {
        super(name);
        addWindowListener(this);
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        Main.exit(0);
    }

    @Override
    public void windowOpened(final WindowEvent e) {
        Main.FRAME.log(LogType.INIT, "ioxbox v " + Main.getVersion() + " running on java " + System.getProperty("java.version"));
        Main.FRAME.log(LogType.INIT, "loading ioxbox");
    }

    @Override
    public void windowClosed(final WindowEvent e) {
        Main.exit(0);
    }

    @Override
    public void windowIconified(final WindowEvent e) {

    }

    @Override
    public void windowDeiconified(final WindowEvent e) {

    }

    @Override
    public void windowActivated(final WindowEvent e) {

    }

    @Override
    public void windowDeactivated(final WindowEvent e) {

    }
}
