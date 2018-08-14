package com.tracy.trpc.consumer.helper;

import com.tracy.trpc.common.model.NodeInfo;

import java.util.Properties;

/**
 * 加载RPC Service配置信息
 *
 * @author tracy
 */
public interface Loader {

    /**
     * 加载配置
     *
     * @return 远程节点信息
     */
    NodeInfo load(Properties properties);
}
