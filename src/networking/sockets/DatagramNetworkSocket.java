package networking.sockets;

import java.io.IOException;
import java.net.*;

public class DatagramNetworkSocket implements NetworkSocket {

    private DatagramSocket socket_in, socket_out;

    public DatagramNetworkSocket(int port) throws SocketException {
        socket_in = new DatagramSocket(port);
        socket_out = new DatagramSocket();
    }

    @Override
    public void joinGroup(SocketAddress address, NetworkInterface iface) throws IOException { }

    @Override
    public void leaveGroup(SocketAddress address, NetworkInterface iface) throws IOException { }

    @Override
    public void close() {
        socket_in.close();
        socket_out.close();
    }

    @Override
    public void send(DatagramPacket packet) throws IOException {
        socket_out.send(packet);
    }

    @Override
    public void receive(DatagramPacket packet) throws IOException {
        socket_in.receive(packet);
    }

    @Override
    public NetworkSocketType getType() {
        return NetworkSocketType.DATAGRAM;
    }
}
