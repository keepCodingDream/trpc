package com.tracy.trpc.register.register;


import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.CommonUtil;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.common.zookeeper.ZookeeperFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import javax.annotation.PreDestroy;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lurenjie on 2017/6/8
 */
@Slf4j
public class ZookeeperRegister implements Register, ConnectionStateListener {
    private CuratorFramework zkClient;
    private List<NodeInfo> serverPath;

    public ZookeeperRegister() throws Exception {
        if (zkClient == null) {
            zkClient = new ZookeeperFactory().getObject();
        }
        serverPath = new ArrayList<>(10);
        zkClient.getConnectionStateListenable().addListener(this);
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
        serverPath.add(nodeInfo);
        createdNode(nodeInfo);
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

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        if (newState == ConnectionState.LOST) {
            try {
                for (NodeInfo info : serverPath) {
                    Stat stat = zkClient.checkExists().forPath(CommonUtil.getZkPath(info));
                    if (stat == null) {
                        createdNode(info);
                    }
                }
            } catch (Exception e) {
                log.error("Deal connect lost error!", e);
            }
        }
    }

    private void createdNode(NodeInfo info) {
        String zkPath = CommonUtil.getZkPath(info);
        while (true) {
            try {
                zkClient.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(zkPath, JSON.toJSONString(info).getBytes(Charset.forName("UTF-8")));
                break;
            } catch (Exception e) {
                log.error("create zk node error", e);
            }
        }
    }
}
