package com.tracy.trpc.common.zookeeper;

import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.util.CommonUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Created by lurenjie on 2017/6/8
 */
public class ZookeeperFactory implements FactoryBean<CuratorFramework> {
    private boolean singleton = true;
    private final static String ROOT = Constants.ZK_PATH_HEAD;
    private CuratorFramework zkClient;

    @Override
    public CuratorFramework getObject() throws Exception {
        if (singleton) {
            if (zkClient == null) {
                zkClient = create();
                zkClient.start();
            }
            return zkClient;
        }
        return create();
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    private CuratorFramework create() throws Exception {
        int sessionTimeout = 30000;
        int connectionTimeout = 30000;
        Properties properties = CommonUtil.getProperties("/trpc.properties");
        String zkHosts = properties.getProperty("trpc.zookeeper.address");
        if (StringUtils.isEmpty(zkHosts)) {
            throw new IllegalArgumentException("trpc.zookeeper.address can not be null in trpc.properties");
        }
        return create(zkHosts, sessionTimeout, connectionTimeout, ROOT);
    }

    private static CuratorFramework create(String connectString, int sessionTimeout, int connectionTimeout, String namespace) {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        return builder.connectString(connectString).sessionTimeoutMs(sessionTimeout).connectionTimeoutMs(connectionTimeout)
                .canBeReadOnly(true).namespace(namespace).retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
                .defaultData(null).build();
    }

    public void close() {
        if (zkClient != null) {
            zkClient.close();
        }
    }
}
