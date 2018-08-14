package com.tracy.trpc.protocol.netty.client;

import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.model.ResponseModel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * @author tracy
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        try {
            ResponseModel responseModel = JSON.parseObject(msg.toString(), ResponseModel.class);
            //是不是搞个异步
            Object content = responseModel.getContent();
        } finally {
            //it is base on the reference count.if not release, It can not collect by GC
            ReferenceCountUtil.release(msg);
        }
    }
}

