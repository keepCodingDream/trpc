package com.tracy.trpc.core;

import java.util.Properties;

/**
 * 服务注册/发现启动类
 *
 * @author tracy.
 * @create 2018-08-14 10:18
 **/
public interface Initializer {

    void start(Properties properties);

    void stop(Properties properties);
}
