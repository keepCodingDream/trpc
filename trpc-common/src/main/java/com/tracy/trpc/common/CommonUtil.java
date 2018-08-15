package com.tracy.trpc.common;

import com.tracy.trpc.common.model.NodeInfo;

/**
 * @author tracy.
 * @create 2018-08-09 11:46
 **/
public class CommonUtil {

    private static final String SPLIT = "/";

    /**
     * 获取zk路径
     *
     * @param nodeInfo 节点信息
     * @return zk地址
     */
    public static String getZkPath(NodeInfo nodeInfo) {
        if (nodeInfo.getIsProvider() != null && nodeInfo.getIsProvider()) {
            return SPLIT + Constants.ZK_PATH_PROVIDER + SPLIT + nodeInfo.getApplicationName() + SPLIT + nodeInfo.getVersion() + SPLIT + nodeInfo.getIp();
        } else {
            return SPLIT + Constants.ZK_PATH_PROVIDER + SPLIT + nodeInfo.getApplicationName() + SPLIT + nodeInfo.getVersion();
        }
    }
}
