package com.tracy.trpc.consumer.strategy;

import com.tracy.trpc.protocol.rpc.RpcCall;
import io.netty.channel.Channel;

/**
 * @author tracy.
 * @create 2018-08-16 10:27
 **/
public interface IStrategy {
    /**
     * 选取一个合适的节点
     *
     * @return 节点信息
     */
    RpcCall select();
}
