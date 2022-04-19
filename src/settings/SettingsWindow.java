package settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SettingsWindow extends JFrame {

    private SettingsView view;

    private SettingsWindow() {
        view = new SettingsView();
        add(view);
        setLocationRelativeTo(null);
        view.setBorder(new EmptyBorder(10, 10,10,10));
        pack();
    }

    private static SettingsWindow singleton = new SettingsWindow();

    public static SettingsWindow getInstance() {
        return singleton;
    }

    public static void showWindow() {
        if (!singleton.isVisible()) singleton.setVisible(true);
        singleton.requestFocus();
    }
}
