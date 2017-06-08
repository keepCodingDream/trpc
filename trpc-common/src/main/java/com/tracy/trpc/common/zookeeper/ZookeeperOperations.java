package com.tracy.trpc.common.zookeeper;

import com.tracy.trpc.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;

/**
 * 封装了zk操作
 * Created by lurenjie on 2017/6/7
 */
@Slf4j
public class ZookeeperOperations {
    private String zookeeperAddress;
    private Integer zookeeperTimeout;

    private ZooKeeper zkClient;

    public ZookeeperOperations(String zookeeperAddress, Integer zookeeperTimeout) {
        this.zookeeperTimeout = zookeeperTimeout;
        this.zookeeperAddress = zookeeperAddress;
    }

    public void init() throws Exception {
        if (StringUtils.isEmpty(zookeeperAddress)) {
            throw new IllegalArgumentException("trpc.zookeeper.address is needed");
        }
        if (zookeeperTimeout == null || zookeeperTimeout < 0) {
            zookeeperTimeout = 3000;
        }
        zkClient = new ZooKeeper(zookeeperAddress, zookeeperTimeout, watchedEvent -> log.info("zk watcher init success! path:{}", watchedEvent.getPath()));
    }

    /**
     * 创建节点
     *
     * @param path 节点地址
     * @param data 节点数据
     * @return the actual path of node
     */
    public String createdNode(String path, String data) throws Exception {
        return zkClient.create(Constants.ZK_PATH_HEAD + path, data.getBytes(Charset.forName("UTF-8")), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
    }

}
