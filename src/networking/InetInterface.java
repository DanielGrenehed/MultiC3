package networking;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class InetInterface {

    private static InetInterface n_interface = null;

    public static InetInterface getInterface() {
        if (n_interface == null) autoSelect();
        return n_interface;
    }

    /* Try to autoselect interface */
    private static void autoSelect() {
        for (NetworkInterface iface: getInterfaces()) {
            try {
                n_interface = new InetInterface(NetworkInterface.getByName(iface.getName()));
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    /* Return a list of all networkInterfaces */
    private static List<NetworkInterface> getInterfaces() {
        List<NetworkInterface> out = new ArrayList<>();

        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (interfaces.hasMoreElements())  {
            NetworkInterface iface = interfaces.nextElement();
            try {
                if (iface.isUp() && !iface.isLoopback()) out.add(iface);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    /* Returns a list of all networkInterface names*/
    public static List<String> getInterfaceNameList() {
        List<String> interface_names = new ArrayList<>();
        for (NetworkInterface iface: getInterfaces()) interface_names.add(iface.getName());
        return interface_names;
    }

    /* Tries to set interface to the provided interface name, if fail then autoselects an interface*/
    public static void setInterface(String iface_name) {
        try {
            if (getInterfaceNameList().contains(iface_name)) n_interface = new InetInterface(NetworkInterface.getByName(iface_name));
            else {
                System.out.println("No network interface named '"+iface_name+"'");
                autoSelect();
            }
        } catch (SocketException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private NetworkInterface iface ;

    private InetInterface(NetworkInterface iface) {
        this.iface = iface;
    }

    public NetworkInterface getNetworkInterface() {
        return iface;
    }



}
