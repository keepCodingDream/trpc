package com.tracy.trpc.register.register;


import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.Constants;
import com.tracy.trpc.common.model.NodeInfo;
import com.tracy.trpc.common.zookeeper.ZookeeperFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * Created by lurenjie on 2017/6/8
 */
@Service
public class ZookeeperRegister implements Register {
    @Resource
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
        zkClient.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/" + Constants.ZK_PATH_PROVIDER + "/" + nodeInfo.getApplicationName() + "/" + nodeInfo.getVersion(), JSON.toJSONString(nodeInfo).getBytes(Charset.forName("UTF-8")));
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
}
