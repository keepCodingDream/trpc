package com.tracy.trpc.register;

import com.tracy.trpc.common.util.CommonUtil;
import com.tracy.trpc.register.helper.ContextInitializer;
import com.tracy.trpc.register.register.Register;
import com.tracy.trpc.register.register.ZookeeperRegister;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * @author tracy
 */
@Slf4j
public class RegisterContextInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Properties properties;
        try {
            properties = CommonUtil.getProperties("/trpc.properties");
        } catch (IOException e) {
            throw new RuntimeException("can not find trpc.properties in current classpath");
        }
        String registerType = properties.getProperty("trpc.context.register.type", "zookeeper");
        if ("zookeeper".equals(registerType)) {
            //for the proxy model
            Register register;
            try {
                register = new ZookeeperRegister();
                ContextInitializer util = new ContextInitializer(register);
                util.init(properties);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
