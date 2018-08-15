package com.tracy.trpc.core;

import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.util.CommonUtil;
import com.tracy.trpc.core.consumer.ConsumerInitializer;
import com.tracy.trpc.core.provider.ProviderInitializer;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * 全局启动器，根据 PROVIDER_NAME、CONSUMER_NAME判断当前是服务提供方还是服务消费方
 *
 * @author tracy.
 * @create 2018-08-14 10:54
 **/
public class ContextInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Properties properties;
        try {
            properties = CommonUtil.getProperties("/trpc.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!StringUtils.isEmpty(properties.getProperty(Constants.PROVIDER_NAME))) {
            Initializer initializer = new ProviderInitializer();
            initializer.start(properties);
        }
        if (!StringUtils.isEmpty(properties.getProperty(Constants.CONSUMER_NAME))) {
            Initializer initializer = new ConsumerInitializer();
            initializer.start(properties);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
