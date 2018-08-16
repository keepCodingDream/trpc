package com.tracy.trpc.protocol.rpc;

import com.tracy.trpc.common.model.InvokeModel;
import com.tracy.trpc.common.model.ResponseModel;
import com.tracy.trpc.protocol.netty.client.NettyClient;
import io.netty.channel.Channel;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Netty协议的封装
 *
 * @author tracy.
 * @create 2018-08-16 14:59
 **/
@Slf4j
public class NettyCall implements RpcCall {

    private static final AtomicLong COUNTER = new AtomicLong();
    public static FastThreadLocal<LongObjectHashMap<Promise<ResponseModel>>> INVOKE_HOLDER = new FastThreadLocal<LongObjectHashMap<Promise<ResponseModel>>>() {
        @Override
        protected LongObjectHashMap<Promise<ResponseModel>> initialValue() throws Exception {
            return new LongObjectHashMap<>();
        }
    };

    private Channel channel;

    private NettyCall() {

    }

    public NettyCall(String host, Integer port) {
        NettyClient client = new NettyClient();
        channel = client.connect(host, port);
    }

    @Override
    public ResponseModel invoke(InvokeModel invokeModel) throws Exception {
        invokeModel.setRequestId(COUNTER.getAndIncrement());
        channel.writeAndFlush(invokeModel);
        return INVOKE_HOLDER.get().remove(invokeModel.getRequestId()).get();

    }
}
