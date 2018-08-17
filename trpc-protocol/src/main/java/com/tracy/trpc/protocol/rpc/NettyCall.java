package com.tracy.trpc.protocol.rpc;

import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.model.InvokeModel;
import com.tracy.trpc.common.model.ResponseModel;
import com.tracy.trpc.protocol.netty.client.NettyClient;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
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
        try {
            channel = client.connect(host, port);
        } catch (InterruptedException e) {
            throw new RuntimeException("Connect to service " + host + ":" + port + " fail");
        }
    }

    @Override
    public ResponseModel invoke(InvokeModel invokeModel) throws Exception {
        invokeModel.setRequestId(COUNTER.getAndIncrement());
        CompositeByteBuf agentRequest = Unpooled.compositeBuffer();
        agentRequest.addComponents(true, Unpooled.copyLong(invokeModel.getRequestId()),
                Unpooled.copiedBuffer(JSON.toJSONString(invokeModel), StandardCharsets.UTF_8));
        channel.writeAndFlush(agentRequest);
        return null;
//        return INVOKE_HOLDER.get().remove(invokeModel.getRequestId()).get();

    }
}
