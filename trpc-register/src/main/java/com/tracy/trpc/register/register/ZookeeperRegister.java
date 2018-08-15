package com.tracy.trpc.register.register;


import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.CommonUtil;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.common.zookeeper.ZookeeperFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;

import javax.annotation.PreDestroy;
import java.nio.charset.Charset;

/**
 * Created by lurenjie on 2017/6/8
 */
@Slf4j
public class ZookeeperRegister implements Register {
    private CuratorFramework zkClient;

    public ZookeeperRegister() throws Exception {
        if (zkClient == null) {
            zkClient = new ZookeeperFactory().getObject();
        }
    }

    /**
     * 创建一个临时节点
     *
     * @param nodeInfo NodeInfo包装的节点信息
     * @throws Exception if zk op fail
     */
    @Override
    public void registry(NodeInfo nodeInfo) throws Exception {
        if (zkClient.getState() == CuratorFrameworkState.LATENT) {
            zkClient.start();
        }
        String zkPath = CommonUtil.getZkPath(nodeInfo);
        zkClient.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(zkPath, JSON.toJSONString(nodeInfo).getBytes(Charset.forName("UTF-8")));
        log.info("=======service :{} registry finish!=======", nodeInfo.getApplicationName());

    }

    @Override
    public void suspend() throws Exception {

    }

    @Override
    public void recovery() throws Exception {

    }

    @Override
    public void unRegistry() throws Exception {

    }

    @PreDestroy
    public void destroy() {
        if (zkClient != null) {
            zkClient.close();
        }
    }
}
