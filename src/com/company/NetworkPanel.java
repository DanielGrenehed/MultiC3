package com.company;

import networking.NetworkConnectionEventListener;
import networking.NetworkController;
import settings.SettingsWindow;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class NetworkPanel extends JPanel implements NetworkConnectionEventListener {

    protected JButton connection_b, settings_b;
    protected JPanel connection_status_box;
    protected boolean n_connected = false;

    public NetworkPanel() {
        setLayout(new GridLayout(1,3));

        /*
        *   Connect/Disconnect button
        * */
        connection_b = new JButton("");
        connection_b.addActionListener(e -> {
            if (n_connected) NetworkController.getInstance().disconnect();
            else NetworkController.getInstance().connect();
        });
        add(connection_b);

        /*
        *   Connection status
        * */
        connection_status_box = new JPanel();
        connection_status_box.setBorder(new LineBorder(this.getBackground(), 10));
        add(connection_status_box);

        /*
        *   Open-settings-panel button
        * */
        settings_b = new JButton("Settings");
        settings_b.addActionListener(e -> {
            SettingsWindow.showWindow();
        });
        add(settings_b);

        /*
        *   Set as event listener
        * */
        NetworkController.getInstance().setNetworkConnectionEventListener(this);
    }


    /*
    *   Respond to network connection event
    * */
    @Override
    public void onNetworkConnectionEvent(boolean connected) {
        n_connected = connected;
        if (connected) {
            connection_b.setText("Disconnect");
            connection_status_box.setBackground(Color.GREEN);
        } else {
            connection_b.setText("Connect");
            connection_status_box.setBackground(Color.RED);
        }
    }
}
