package networking.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.NetworkInterface;
import java.net.SocketAddress;

public interface NetworkSocket {

    void joinGroup(SocketAddress address, NetworkInterface iface) throws IOException;

    void leaveGroup(SocketAddress address, NetworkInterface iface) throws IOException;

    void close();

    void send(DatagramPacket packet) throws IOException;

    void receive(DatagramPacket packet) throws IOException;

    NetworkSocketType getType();
}
