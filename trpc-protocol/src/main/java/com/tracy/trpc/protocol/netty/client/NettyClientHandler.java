package com.tracy.trpc.protocol.netty.client;

import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.model.ResponseModel;
import com.tracy.trpc.protocol.rpc.AbstractCall;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tracy
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        log.error("NettyClientHandler error", cause.fillInStackTrace());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.info("Received from netty server");
        int length = msg.readInt();
        byte[] content = new byte[length];
        msg.readBytes(content);
        ResponseModel responseModel = JSON.parseObject(new String(content), ResponseModel.class);
        AbstractCall.INVOKE_HOLDER.get(responseModel.getRequestId()).trySuccess(responseModel);
    }
}

