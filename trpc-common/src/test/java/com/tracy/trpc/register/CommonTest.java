package com.tracy.trpc.register;

import com.tracy.trpc.common.ClassHelper;
import com.tracy.trpc.common.IpHelper;

import java.net.SocketException;
import java.util.Set;

/**
 * Created by lurenjie on 2017/6/7
 */
public class CommonTest {
    public static void main(String[] args) throws SocketException {
        Set<Class<?>> classes = ClassHelper.getClasses("org.apache.log4j.chainsaw");
        for (Class item : classes) {
            System.out.println(item.getName());
        }
        System.out.println(IpHelper.localIpAddress());
    }
}
