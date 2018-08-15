package com.tracy.trpc.common;

/**
 * Created by lurenjie on 2017/6/7
 */
public class Constants {
    public static final String ZK_PATH_HEAD = "trpc";
    public static final String ZK_PATH_PROVIDER = "provider";
    public static final String ZK_PATH_CONSUMER = "consumer";
    public static final String ZK_NAME = "zookeeper";
    public static final String NETTY_NAME = "netty";

    // config start ========================
    //provider  ----------------------------

    public static final String PROVIDER_NAME = "trpc.context.provider.name";
    public static final String PROVIDER_VERSION = "trpc.context.provider.version";
    public static final String PROVIDER_IP = "trpc.context.provider.ip";
    public static final String PROVIDER_PORT = "trpc.context.provider.port";

    //consumer  ----------------------------

    public static final String CONSUMER_NAME = "trpc.context.consumer.name";
    public static final String CONSUMER_VERSION = "trpc.context.consumer.version";
    public static final String CONSUMER_IP = "trpc.context.consumer.ip";
    public static final String CONSUMER_PORT = "trpc.context.consumer.port";

    public static final String BASE_PACKAGE = "trpc.context.base.package";
    public static final String REGISTER_TYPE = "trpc.context.register.type";
    public static final String PROTOCOL_TYPE = "trpc.context.application.protocol";

    // config end =========================
}
