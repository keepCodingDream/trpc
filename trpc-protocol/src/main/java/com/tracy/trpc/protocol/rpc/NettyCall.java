package com.tracy.trpc.protocol.rpc;

import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.model.InvokeModel;
import com.tracy.trpc.common.model.ResponseModel;
import com.tracy.trpc.protocol.netty.client.NettyClient;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultPromise;
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
public class NettyCall extends AbstractCall {

    private static final AtomicLong COUNTER = new AtomicLong();

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
        Promise<ResponseModel> promise = new DefaultPromise<>(channel.eventLoop());
        CompositeByteBuf agentRequest = Unpooled.compositeBuffer();
        String request = JSON.toJSONString(invokeModel);
        agentRequest.addComponents(true, Unpooled.copyInt(request.length()),
                Unpooled.copiedBuffer(request, StandardCharsets.UTF_8));
        channel.writeAndFlush(agentRequest);
        INVOKE_HOLDER.put(invokeModel.getRequestId(), promise);
        return promise.get();
    }
}
