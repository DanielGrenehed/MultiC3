import com.company.ChattWindow;
import networking.InetInterface;
import networking.NetworkController;
import networking.sockets.NetworkSocketType;

public class Main {

    public static void main(String[] args) {
        String hostname = "127.0.0.1";
        int port = 12540;
        new ChattWindow("UDP - Chatt client");
        InetInterface.setInterface("en0");
        NetworkController.getInstance().setup(hostname, port, port,"234.235.236.237", NetworkSocketType.MULTICAST);
    }
}
