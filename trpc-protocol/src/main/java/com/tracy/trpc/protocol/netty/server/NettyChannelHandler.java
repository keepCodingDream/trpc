package com.tracy.trpc.protocol.netty.server;

import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.context.DefaultProxyContext;
import com.tracy.trpc.common.model.InvokeModel;
import com.tracy.trpc.common.model.ResponseModel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.lang.reflect.Method;

/**
 * @author tracy
 */
public class NettyChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        try {
            InvokeModel content = JSON.parseObject(msg.toString(), InvokeModel.class);
            Object inst = DefaultProxyContext.getInstants().getBean(content.getInterfaceName());
            Method method = inst.getClass().getMethod(content.getMethod(), content.getParamsCls());
            Object result = method.invoke(inst, content.getInvokeParams());
            ResponseModel response = new ResponseModel();
            response.setTProtocol(content.getTProtocol());
            response.setContent(result);
            //todo 是不是搞个异步
            ctx.writeAndFlush(response);
        } finally {
            //it is base on the reference count.if not release, It can not collect by GC
            ReferenceCountUtil.release(msg);
        }
    }
}

