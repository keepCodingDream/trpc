package com.tracy.trpc.core.consumer;

import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.consumer.core.ConsumerProxyFactory;
import com.tracy.trpc.consumer.helper.Loader;
import com.tracy.trpc.consumer.helper.ZookeeperLoader;
import com.tracy.trpc.core.Initializer;

import java.util.List;
import java.util.Properties;

/**
 * @author tracy.
 * @create 2018-08-14 10:26
 **/
public class ConsumerInitializer implements Initializer {
    @Override
    public void start(Properties properties) {
        String registerType = properties.getProperty(Constants.REGISTER_TYPE, Constants.ZK_NAME);
        Loader loader;
        if (Constants.ZK_NAME.equals(registerType)) {
            try {
                loader = new ZookeeperLoader();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Can not support trpc.context.register.type:" + registerType);
        }
        List<NodeInfo> nodeList = loader.load(properties);
        String appName = properties.getProperty(Constants.CONSUMER_NAME);
        ConsumerProxyFactory factory = new ConsumerProxyFactory();
        factory.createProxy(appName, nodeList);
    }

    @Override
    public void stop(Properties properties) {

    }
}
