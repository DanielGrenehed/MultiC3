package settings;

import gui.LabeledComponent;
import networking.InetInterface;
import networking.NetworkController;
import networking.sockets.NetworkSocketType;

import javax.swing.*;
import java.awt.*;

public class SettingsView extends JPanel {

    protected JTextField hostname_tf, tx_port_tf, rx_port_tf, group_tf, nick_tf;
    protected JComboBox<NetworkSocketType> type_cb;
    protected JComboBox<String> iface_cb;
    protected JButton apply_btn;

    /*
    *   Load current settings into view
    * */
    public SettingsView() {
        setLayout(new GridLayout(8, 1));
        NetworkController nc = NetworkController.getInstance();

        hostname_tf = new JTextField(nc.getHostname());
        add(new LabeledComponent("Hostname", hostname_tf));

        tx_port_tf = new JTextField(String.valueOf(nc.getTransmitPort()));
        add(new LabeledComponent("Transmit on port", tx_port_tf));

        rx_port_tf = new JTextField(String.valueOf(nc.getReceivePort()));
        add(new LabeledComponent("Receive on port", rx_port_tf));

        DefaultComboBoxModel<NetworkSocketType> sModel = new DefaultComboBoxModel<>(NetworkSocketType.values());
        type_cb = new JComboBox<>(sModel);
        type_cb.setSelectedItem(nc.getType());
        add(new LabeledComponent("Protocol", type_cb));

        iface_cb = new JComboBox<>(new DefaultComboBoxModel<>(InetInterface.getInterfaceNameList().toArray(new String[0])));
        iface_cb.setSelectedItem(InetInterface.getInterface().getNetworkInterface().getName());
        add(new LabeledComponent("Interface", iface_cb));

        group_tf = new JTextField(nc.getMulticastHostname());
        add(new LabeledComponent("Multicast address", group_tf));

        nick_tf = new JTextField(nc.getPacketHeader());
        add(new LabeledComponent("Identifier", nick_tf));

        apply_btn = new JButton("Apply");
        add(apply_btn);
        apply_btn.addActionListener(e -> { apply(); });
    }


    /*
    * Get input values and setup the NetworkController with the new values
    * */
    private void apply() {
        NetworkController nc = NetworkController.getInstance();
        nc.disconnect();

        String hostname = hostname_tf.getText();
        int tx_port = Integer.parseInt(tx_port_tf.getText());
        int rx_port = Integer.parseInt(rx_port_tf.getText());
        NetworkSocketType type = (NetworkSocketType) type_cb.getSelectedItem();
        String group = group_tf.getText();
        String nick = nick_tf.getText();
        InetInterface.setInterface((String)iface_cb.getSelectedItem());

        nc.setPacketHeader(nick);
        nc.setup(hostname, tx_port, rx_port, group, type);
    }
}
