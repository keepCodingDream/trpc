package com.tracy.trpc.common.zookeeper;

import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 封装了zk操作
 * Created by lurenjie on 2017/6/7
 */
@Slf4j
@Deprecated
public class ZookeeperOperations {
    private static String zookeeperAddress;
    private CuratorFramework zkClient;

    static {
        Properties properties;
        try {
            properties = CommonUtil.getProperties("/trpc.properties");
        } catch (Exception e) {
            throw new RuntimeException("read trpc.properties出错");
        }
        zookeeperAddress = properties.getProperty("trpc.zookeeper.address");
    }


    public void connect() throws Exception {
        if (zkClient.getState().equals(CuratorFrameworkState.STOPPED)) {
            return;
        }
        if (StringUtils.isEmpty(zookeeperAddress)) {
            throw new IllegalArgumentException("trpc.zookeeper.address is needed");
        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(2000, 30);
        zkClient = CuratorFrameworkFactory.newClient(zookeeperAddress, retryPolicy);
    }

    /**
     * 创建节点
     *
     * @param path 节点地址
     * @param data 节点数据
     * @return the actual path of node
     */
    public String createdNode(String path, String data) throws Exception {
        return zkClient.create().forPath(Constants.ZK_PATH_HEAD + path, data.getBytes(Charset.forName("UTF-8")));
    }

}
