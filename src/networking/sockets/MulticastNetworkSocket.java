package networking.sockets;

import java.io.IOException;
import java.net.*;

public class MulticastNetworkSocket implements NetworkSocket {

    private MulticastSocket socket_in, socket_out;

    public MulticastNetworkSocket(int port) throws IOException {
        socket_in = new MulticastSocket(port);
        socket_out = new MulticastSocket();
    }

    @Override
    public void joinGroup(SocketAddress address, NetworkInterface iface) throws IOException {
        socket_in.joinGroup(address, iface);
    }

    @Override
    public void leaveGroup(SocketAddress address, NetworkInterface iface) throws IOException {
        socket_in.leaveGroup(address, iface);
    }

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
        return NetworkSocketType.MULTICAST;
    }
}
