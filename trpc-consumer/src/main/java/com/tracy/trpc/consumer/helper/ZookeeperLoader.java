package com.tracy.trpc.consumer.helper;

import com.tracy.trpc.common.CommonUtil;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.common.zookeeper.ZookeeperFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;

/**
 * @author tracy.
 * @create 2018-08-13 16:30
 **/
@Slf4j
public class ZookeeperLoader implements Loader {
    @Resource
    private CuratorFramework zkClient;

    public ZookeeperLoader() {
        if (zkClient == null) {
            try {
                zkClient = new ZookeeperFactory().getObject();
            } catch (Exception e) {
                log.error("", e);
                throw new RuntimeException("connect to zookeeper error!");
            }
        }
    }

    @Override
    public NodeInfo load(Properties properties) {
        String appName = properties.getProperty("trpc.context.application.name");
        String appVersion = properties.getProperty("trpc.context.application.version", "0.0.1");
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setApplicationName(appName);
        nodeInfo.setVersion(appVersion);
        try {
            List<String> serviceList = zkClient.getChildren().forPath(CommonUtil.getZkPath(nodeInfo));

        } catch (Exception e) {
            log.error("search zk error!", e);
            throw new RuntimeException(e);
        }
        return null;
    }
}
