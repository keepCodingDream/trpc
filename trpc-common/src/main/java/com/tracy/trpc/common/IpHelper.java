package com.tracy.trpc.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by lurenjie on 2017/6/7
 */
public class IpHelper {
    public static String localIpAddress() throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    if (!"127.0.0.1".equals(ip.getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        return "";
    }

}
