package com.company;

import networking.NetworkController;

public class NetworkedMessageReceiver implements MessageReceiver {

    private MessageReceiver loopback;

    public NetworkedMessageReceiver(MessageReceiver loopback) {
        this.loopback = loopback;
    }

    /*
    *   Send message on network
    *   send to loopback if
    *   packet will not loopback
    *   on network
    * */
    @Override
    public void onMessage(String str) {
        NetworkController nc = NetworkController.getInstance();
        if (!nc.loopback()) loopback.onMessage(str);
        nc.send(str);
    }
}
