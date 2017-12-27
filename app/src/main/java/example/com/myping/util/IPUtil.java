package example.com.myping.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 */

public class IPUtil {

    public static long getIP(String ipStr) {
        try {
            InetAddress ip = InetAddress.getByName(ipStr);
            byte[] b = ip.getAddress();
            long l = b[0] << 24L & 0xff000000L |
                    b[1] << 16L & 0xff0000L |
                    b[2] << 8L & 0xff00L |
                    b[3] & 0xffL;
            return l;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String toIP(long ip) {
        byte[] b = new byte[4];
        int i = (int) ip;//低32位
        b[0] = (byte) ((i >> 24) & 0x000000ff);
        b[1] = (byte) ((i >> 16) & 0x000000ff);
        b[2] = (byte) ((i >> 8) & 0x000000ff);
        b[3] = (byte) ((i) & 0x000000ff);
        try {
            InetAddress byAddress = InetAddress.getByAddress(b);
            return byAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
