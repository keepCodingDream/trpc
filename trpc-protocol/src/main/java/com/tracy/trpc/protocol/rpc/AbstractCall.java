package com.tracy.trpc.protocol.rpc;

import com.tracy.trpc.common.model.ResponseModel;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tracy.
 * @create 2018-08-18 11:52
 **/
public abstract class AbstractCall implements RpcCall {
    public static Map<Long, Promise<ResponseModel>> INVOKE_HOLDER = new ConcurrentHashMap<>(2 << 20);
}
