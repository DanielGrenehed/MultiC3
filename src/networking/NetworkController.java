package networking;

import networking.sockets.DatagramNetworkSocket;
import networking.sockets.MulticastNetworkSocket;
import networking.sockets.NetworkSocket;
import networking.sockets.NetworkSocketType;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class NetworkController implements Runnable {

    private static NetworkController singleton = new NetworkController();

    /**/
    public static NetworkController getInstance() {
        return singleton;
    }

    private NetworkController() {}

    protected String hostname;
    protected int receive_port;
    protected int transmit_port;

    protected String multicast_hostname;
    protected String packet_header = "";

    protected NetworkSocketType type;
    protected NetworkSocket socket;

    protected InetAddress address;
    protected Thread network_receiver_thread;

    protected boolean connected;
    private boolean setup_successful = false;

    protected NetworkConnectionEventListener connection_listener;
    protected PacketActionListener packet_listener;

    /*
    *   Set parameters and initialize connection
    * */
    public void setup(String hostname, int txp, int rxp, String group, NetworkSocketType type) {
        transmit_port = txp;
        receive_port = rxp;
        this.hostname = hostname;
        this.multicast_hostname = group;
        this.type = type;
        connect();
    }

    public void setup(String hostname, int port, String group, NetworkSocketType type) {
        setup(hostname, port, port, group, type);
    }

    public void setup(String hostname, int port) {
        setup(hostname, port, port, "", NetworkSocketType.DATAGRAM);
    }



    /*
    *   Create inetAddress
    * */
    private void createAddress() {
        try {
            if (type == NetworkSocketType.MULTICAST) address = InetAddress.getByName(multicast_hostname);
            else address = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            setup_successful = false;
            return;
        }
    }

    /*
    *   Construct socket by type
    * */
    private void createSocket() {
        try {
            switch (type) {
                case DATAGRAM:
                    socket = new DatagramNetworkSocket(receive_port);
                    System.out.println("Created DATAGRAM socket");
                    break;
                case MULTICAST:
                    socket = new MulticastNetworkSocket(receive_port);
                    System.out.println("Created MULTICAST socket");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            setup_successful = false;
        }
    }

    /*
    *   Join group on socket
    * */
    private void joinGroup() {
        try { socket.joinGroup(new InetSocketAddress(InetAddress.getByName(multicast_hostname), receive_port), InetInterface.getInterface().getNetworkInterface()); }
        catch (IOException e) {
            e.printStackTrace();
            setup_successful = false;
        }
    }

    /*
    *   Set packet header to localhosts name if not initialized
    * */
    private void setDefaultHeader() {
        if (packet_header == "") {
            try { packet_header = InetAddress.getLocalHost().getHostName(); }
            catch (UnknownHostException e) { e.printStackTrace(); }
        }
    }

    /*
    *   Start network receiver thread
    * */
    private void startNetworkReceiver() {
        if (connected) {
            network_receiver_thread = new Thread(this);
            network_receiver_thread.start();
        } else System.out.println("Cannot receive when not connected");
    }

    /*
    *   Create socket and start receiving
    * */
    public void connect() {
        setDefaultHeader();
        setup_successful = true;

        createAddress();
        createSocket();
        joinGroup();

        if (setup_successful) connected = true;

        startNetworkReceiver();
    }

    /*
     *  Disconnect sockets and close receiver thread
     * */
    public void disconnect() {
        connected = false;

        try { socket.leaveGroup(new InetSocketAddress(InetAddress.getByName(multicast_hostname), receive_port), InetInterface.getInterface().getNetworkInterface()); }
        catch (IOException e) { e.printStackTrace(); }

        socket.close();

        try { network_receiver_thread.join(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    /*
    *   Construct a datagram packet ready for transmission
    * */
    private DatagramPacket createPacket(String str) {
        byte[] buffer = (packet_header + ": " + str).getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, transmit_port);
        return packet;
    }

    /*
    *   Constructs a packet with str and sends it
    * */
    public void send(String str) {
        if (!connected) {
            System.out.println("Network not connected!");
            return;
        }
        try { socket.send(createPacket(str)); }
        catch (IOException e) { e.printStackTrace(); }
    }

    /*
    *   Emits event containing current state of the network connection.
    * */
    protected void notifyEventListener() {
        if (connection_listener != null) connection_listener.onNetworkConnectionEvent(connected);
    }

    /*
    *   Emit event containing message received
    * */
    protected void notifyPacketListener(String message, String source) {
        if (packet_listener != null) packet_listener.onPacketReceived(message, source);
    }

    /*
     *  Wait for next packet and notify
     *   packet listener
     * */
    protected void nextPacket() throws IOException, SocketException {
        byte[] buffer = new byte[512];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        notifyPacketListener(new String(buffer, 0, packet.getLength()), packet.getAddress().getHostAddress());
    }

    /*
    *   Wait for next packet and notify
    *   packet listener and catch any exception
    * */
    protected void catchNextPacket() {
        try { nextPacket(); }
        catch (SocketException se) { if (connected) se.printStackTrace(); }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }

    /*
    *   Waits for messages on socket and calls
    *   listener when messages are received
    * */
    @Override
    public void run() {
        notifyEventListener();
        while(connected) catchNextPacket();
        notifyEventListener();
    }



    public int getReceivePort() {
        return receive_port;
    }

    public int getTransmitPort() {
        return transmit_port;
    }

    public String getHostname() {
        return hostname;
    }

    public String getMulticastHostname() {
        return multicast_hostname;
    }

    public NetworkSocketType getType() {
        return type;
    }

    public String getPacketHeader() {
        return packet_header;
    }

    public void setPacketHeader(String hdr) {
        packet_header = hdr;
    }

    public void setNetworkConnectionEventListener(NetworkConnectionEventListener ncel) {
        connection_listener = ncel;
    }

    public void setPacketActionListener(PacketActionListener pal) {
        packet_listener = pal;
    }

    /*
    *   Returns true if locally submitted packets will be received back locally
    * */
    public boolean loopback() {
        if (receive_port == transmit_port) {
            if (type == NetworkSocketType.DATAGRAM && (hostname.equals("127.0.0.1") || hostname.equals("localhost"))) {
                return true;
            } else if (type == NetworkSocketType.MULTICAST) {
                return true;
            }
        }
        return false;
    }
}
