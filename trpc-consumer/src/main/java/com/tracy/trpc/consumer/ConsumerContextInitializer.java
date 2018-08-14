package com.tracy.trpc.consumer;

import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.common.util.CommonUtil;
import com.tracy.trpc.consumer.core.ConsumerProxyFactory;
import com.tracy.trpc.consumer.helper.Loader;
import com.tracy.trpc.consumer.helper.ZookeeperLoader;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * @author tracy
 */
@Slf4j
public class ConsumerContextInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Properties properties;
        try {
            properties = CommonUtil.getProperties("/trpc.properties");
        } catch (IOException e) {
            throw new RuntimeException("can not find trpc.properties in current classpath");
        }
        String registerType = properties.getProperty("trpc.context.register.type", "zookeeper");
        Loader loader = null;
        if ("zookeeper".equals(registerType)) {
            try {
                loader = new ZookeeperLoader();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Can not support trpc.context.register.type:" + registerType);
        }
        NodeInfo info = loader.load();
        ConsumerProxyFactory factory = new ConsumerProxyFactory();
        factory.createProxy(info);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
