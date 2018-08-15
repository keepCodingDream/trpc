package com.tracy.trpc.consumer.helper;

import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.CommonUtil;
import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.common.zookeeper.ZookeeperFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private static final String SPLIT = "/";

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
    public List<NodeInfo> load(Properties properties) {
        String appName = properties.getProperty(Constants.CONSUMER_NAME);
        String appVersion = properties.getProperty(Constants.CONSUMER_VERSION, "0.0.1");
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setApplicationName(appName);
        nodeInfo.setVersion(appVersion);
        try {
            String zkPath = CommonUtil.getZkPath(nodeInfo);
            //this is the list of service ip
            List<String> serviceList = zkClient.getChildren().forPath(CommonUtil.getZkPath(nodeInfo));
            List<NodeInfo> result = new ArrayList<>(serviceList.size());
            for (String item : serviceList) {
                result.add(JSON.parseObject(zkClient.getData().forPath(zkPath + SPLIT + item), NodeInfo.class));
            }
            return result;
        } catch (Exception e) {
            log.error("search zk error!", e);
            throw new RuntimeException(e);
        }
    }
}
