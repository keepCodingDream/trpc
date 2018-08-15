package com.tracy.trpc.core.provider;

import com.tracy.trpc.common.Constants;
import com.tracy.trpc.core.Initializer;
import com.tracy.trpc.protocol.netty.server.NettyServer;
import com.tracy.trpc.register.helper.ContextInitializer;
import com.tracy.trpc.register.register.Register;
import com.tracy.trpc.register.register.ZookeeperRegister;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author tracy.
 * @create 2018-08-14 10:26
 **/
public class ProviderInitializer implements Initializer {
    @Override
    public void start(Properties properties) {
        String registerType = properties.getProperty(Constants.REGISTER_TYPE, Constants.ZK_NAME);
        if (Constants.ZK_NAME.equals(registerType)) {
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
        String protocol = properties.getProperty(Constants.PROTOCOL_TYPE);
        if (StringUtils.isEmpty(protocol)) {
            protocol = Constants.NETTY_NAME;
        }
        if (Constants.NETTY_NAME.equals(protocol)) {
            NettyServer nettyServer = new NettyServer();
            String port = properties.getProperty(Constants.PROVIDER_PORT);
            port = StringUtils.isEmpty(port) ? "9090" : port;
            nettyServer.startServer(Integer.valueOf(port));
        } else {
            throw new RuntimeException("protocol : " + protocol + " is not support");
        }
    }

    @Override
    public void stop(Properties properties) {

    }
}
