package com.tracy.trpc.core.consumer;

import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.consumer.core.ConsumerProxyFactory;
import com.tracy.trpc.consumer.helper.Loader;
import com.tracy.trpc.consumer.helper.ZookeeperLoader;
import com.tracy.trpc.core.Initializer;
import com.tracy.trpc.protocol.rpc.NettyCall;
import com.tracy.trpc.protocol.rpc.RpcCall;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
        if (CollectionUtils.isEmpty(nodeList)) {
            throw new RuntimeException("Remote server not exist!");
        }
        List<RpcCall> calls = connect2Server(properties, nodeList);
        String appName = properties.getProperty(Constants.CONSUMER_NAME);
        ConsumerProxyFactory factory = new ConsumerProxyFactory();
        factory.createProxy(properties, appName, calls);
    }

    private List<RpcCall> connect2Server(Properties properties, List<NodeInfo> nodeList) {
        String protocol = properties.getProperty(Constants.PROTOCOL_TYPE, Constants.NETTY_NAME);
        List<RpcCall> calls = new ArrayList<>(nodeList.size());
        for (NodeInfo info : nodeList) {
            if (Constants.NETTY_NAME.equals(protocol)) {
                RpcCall call = new NettyCall(info.getIp(), Integer.valueOf(info.getPort()));
                calls.add(call);
            } else {
                throw new IllegalArgumentException("protocol type: " + Constants.PROTOCOL_TYPE + " do not support");
            }
        }
        return calls;
    }

    @Override
    public void stop(Properties properties) {

    }
}
