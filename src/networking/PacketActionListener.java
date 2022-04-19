package networking;

public interface PacketActionListener {
    void onPacketReceived(String packet, String host);
}
