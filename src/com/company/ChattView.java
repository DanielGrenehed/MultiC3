package com.company;

import gui.InputField;
import gui.Console;
import networking.NetworkController;
import networking.PacketActionListener;

import javax.swing.*;
import java.awt.*;

public class ChattView extends JPanel implements PacketActionListener {

    protected NetworkPanel top_bar;
    protected Console console;
    protected InputField input;

    public ChattView() {
        setLayout(new BorderLayout());
        NetworkController.getInstance().setPacketActionListener(this);

        top_bar = new NetworkPanel();
        add(top_bar, BorderLayout.PAGE_START);

        console = new Console();
        add(console, BorderLayout.CENTER);

        input = new InputField(new NetworkedMessageReceiver(console));
        add(input, BorderLayout.PAGE_END);
    }

    @Override
    public void onPacketReceived(String packet, String host) {
        console.onMessage(host + ": "+ packet);
    }

}
