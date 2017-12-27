package example.com.myping.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetworkHelper {
    private static NetworkHelper instance = null;

    public static synchronized NetworkHelper getInstance() {
        if (instance == null) {
            instance = new NetworkHelper();
        }
        return instance;

    }

    /**
     * 测试本地能否ping ip
     *
     * @param ip
     * @return
     */
    public boolean isReachIp(String ip) {
        boolean isReach = false;
        try {
            InetAddress address = InetAddress.getByName(ip);// ping this IP

            if (address instanceof java.net.Inet4Address) {
                Log.e("111", ip + " is ipv4 address");
            } else if (address instanceof java.net.Inet6Address) {
                Log.e("111", ip + " is ipv6 address");
            } else {
                Log.e("111", ip + " is unrecongized");
            }
            if (address.isReachable(5000)) {
                isReach = true;
                Log.e("111", "SUCCESS - ping " + ip + " with no interface specified");
            } else {
                isReach = false;
                Log.e("111", "FAILURE - ping " + ip + " with no interface specified");
            }
        } catch (Exception e) {
            Log.e("111", "error occurs:" + e.getMessage());
        }
        return isReach;
    }

    /**
     * 测试本地所有的网卡地址都能ping通 ip
     *
     * @param ip
     * @return
     */
    public boolean isReachNetworkInterfaces(String ip) {
        boolean isReach = false;
        try {
            InetAddress address = InetAddress.getByName(ip);// ping this IP

            if (address instanceof java.net.Inet4Address) {
                Log.e("111", ip + " is ipv4 address");
            } else if (address instanceof java.net.Inet6Address) {
                Log.e("111", ip + " is ipv6 address");
            } else {
                Log.e("111", ip + " is unrecongized");
            }
            if (address.isReachable(5000)) {
                isReach = true;
                Log.e("111", "SUCCESS - ping " + ip
                        + " with no interface specified");
            } else {
                isReach = false;
                Log.e("111", "FAILURE - ping " + ip
                        + " with no interface specified");
            }
            if (isReach) {
                Log.e("111", "-------Trying different interfaces--------");
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface
                        .getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    Log.e("111", "Checking interface, DisplayName:"
                            + ni.getDisplayName() + ", Name:" + ni.getName());
                    if (address.isReachable(ni, 0, 5000)) {
                        isReach = true;
                        Log.e("111", "SUCCESS - ping " + ip);
                    } else {
                        isReach = false;
                        Log.e("111", "FAILURE - ping " + ip);
                    }
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        Log.e("111", "IP: " + ips.nextElement().getHostAddress());
                    }
                    Log.e("111", "-----------------check now NetworkInterface is done--------------------------");
                }
            }
        } catch (Exception e) {
            Log.e("111", "error occurs:" + e.getMessage());
        }
        return isReach;
    }

    /**
     * 获取能与远程主机指定端口建立连接的本机ip地址
     *
     * @param remoteAddr
     * @param port
     * @return
     */
    public String getReachableIP(InetAddress remoteAddr, int port) {
        String retIP = null;
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> localAddrs = ni.getInetAddresses();
                while (localAddrs.hasMoreElements()) {
                    InetAddress localAddr = localAddrs.nextElement();
                    if (isReachable(localAddr, remoteAddr, port, 5000)) {
                        retIP = localAddr.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("111", "Error occurred while listing all the local network addresses:"
                    + e.getMessage());
        }
        if (retIP == null) {
            Log.e("111", "NULL reachable local IP is found!");
        } else {
            Log.e("111", "Reachable local IP is found, it is " + retIP);
        }
        return retIP;
    }

    /**
     * 获取能与远程主机指定端口建立连接的本机ip地址
     *
     * @param remoteIp
     * @param port
     * @return
     */
    public String getReachableIP(String remoteIp, int port) {

        String retIP = null;
        InetAddress remoteAddr = null;
        Enumeration<NetworkInterface> netInterfaces;
        try {
            remoteAddr = InetAddress.getByName(remoteIp);
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> localAddrs = ni.getInetAddresses();
                while (localAddrs.hasMoreElements()) {
                    InetAddress localAddr = localAddrs.nextElement();
                    if (isReachable(localAddr, remoteAddr, port, 5000)) {
                        retIP = localAddr.getHostAddress();
                        break;
                    }
                }
            }
        } catch (UnknownHostException e) {
            Log.e("111", "Error occurred while listing all the local network addresses:" + e.getMessage());
        } catch (SocketException e) {
            Log.e("111", "Error occurred while listing all the local network addresses:" + e.getMessage());
        }
        if (retIP == null) {
            Log.e("111", "NULL reachable local IP is found!");
        } else {
            Log.e("111", "Reachable local IP is found, it is " + retIP);
        }
        return retIP;
    }

    /**
     * 测试localInetAddr能否与远程的主机指定端口建立连接相连
     *
     * @param localInetAddr
     * @param remoteInetAddr
     * @param port
     * @param timeout
     * @return
     */
    public boolean isReachable(InetAddress localInetAddr,
                               InetAddress remoteInetAddr, int port, int timeout) {
        boolean isReachable = false;
        Socket socket = null;
        try {
            socket = new Socket();
            // 端口号设置为 0 表示在本地挑选一个可用端口进行连接
            SocketAddress localSocketAddr = new InetSocketAddress(
                    localInetAddr, 0);
            socket.bind(localSocketAddr);
            InetSocketAddress endpointSocketAddr = new InetSocketAddress(
                    remoteInetAddr, port);
            socket.connect(endpointSocketAddr, timeout);
            Log.e("111", "SUCCESS - connection established! Local: "
                    + localInetAddr.getHostAddress() + " remote: "
                    + remoteInetAddr.getHostAddress() + " port" + port);
            isReachable = true;
        } catch (IOException e) {
            Log.e("111", "FAILRE - CAN not connect! Local: "
                    + localInetAddr.getHostAddress() + " remote: "
                    + remoteInetAddr.getHostAddress() + " port" + port);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e("111", "Error occurred while closing socket:"
                            + e.getMessage());
                }
            }
        }
        return isReachable;
    }

    /**
     * 测试localIp能否与远程的主机指定端口建立连接相连
     *
     * @param localIp
     * @param remoteIp
     * @param port
     * @param timeout
     * @return
     */
    public boolean isReachable(String localIp, String remoteIp,
                               int port, int timeout) {
        boolean isReachable = false;
        Socket socket = null;
        InetAddress localInetAddr = null;
        InetAddress remoteInetAddr = null;
        try {
            localInetAddr = InetAddress.getByName(localIp);
            remoteInetAddr = InetAddress.getByName(remoteIp);
            socket = new Socket();
            // 端口号设置为 0 表示在本地挑选一个可用端口进行连接
            SocketAddress localSocketAddr = new InetSocketAddress(
                    localInetAddr, 0);
            socket.bind(localSocketAddr);
            InetSocketAddress endpointSocketAddr = new InetSocketAddress(
                    remoteInetAddr, port);
            socket.connect(endpointSocketAddr, timeout);
            Log.e("111", "SUCCESS - connection established! Local: "
                    + localInetAddr.getHostAddress() + " remote: "
                    + remoteInetAddr.getHostAddress() + " port" + port);
            isReachable = true;
        } catch (IOException e) {
            Log.e("111", "FAILRE - CAN not connect! Local: "
                    + localInetAddr.getHostAddress() + " remote: "
                    + remoteInetAddr.getHostAddress() + " port" + port);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e("111", "Error occurred while closing socket:"
                            + e.getMessage());
                }
            }
        }
        return isReachable;
    }

    public static void main(String[] args) {
        if (NetworkHelper.getInstance().isReachIp("192.168.126.128")) {
            Log.e("111", "=======本机可以ping通ip：" + "192.168.126.128");
        } else {
            Log.e("111", "=======本机ping不通ip：" + "192.168.126.128");
        }
        if (NetworkHelper.getInstance().isReachNetworkInterfaces("192.168.126.128")) {
            Log.e("111", "=======本机所有网卡可以ping通ip：" + "192.168.126.128");
        } else {
            Log.e("111", "=======本机所有网卡ping不通ip：" + "192.168.126.128");
        }
        String localIp = NetworkHelper.getInstance().getReachableIP("192.168.126.128", 8081);
        if (!TextUtils.isEmpty(localIp)) {
            Log.e("111", "=======本机可以与ip：" + "192.168.126.128" + ",port:" + 8081 + "建立连接的IP：" + localIp);
        } else {
            Log.e("111", "=======本机不能与ip：" + "192.168.126.128" + ",port:" + 8081 + "建立连接的IP");
        }
    }

}