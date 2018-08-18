package com.tracy.trpc.protocol.netty.server;

import com.alibaba.fastjson.JSON;
import com.tracy.trpc.common.context.DefaultProxyContext;
import com.tracy.trpc.common.model.InvokeModel;
import com.tracy.trpc.common.model.ResponseModel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * @author tracy
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        log.error(" exceptionCaught ", cause.fillInStackTrace());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        try {
            log.info("request received!");
            int length = msg.readInt();
            byte[] content = new byte[length];
            msg.readBytes(content);
            InvokeModel invokeModel = JSON.parseObject(new String(content), InvokeModel.class);
            Object inst = DefaultProxyContext.getInstants().getBean(invokeModel.getInterfaceName());
            Method method = inst.getClass().getMethod(invokeModel.getMethod(), invokeModel.getParamsCls());
            Object result = method.invoke(inst, invokeModel.getInvokeParams());
            ResponseModel response = new ResponseModel();
            response.setTProtocol(invokeModel.getTProtocol());
            response.setContent(result);
            response.setRequestId(invokeModel.getRequestId());
            CompositeByteBuf agentRequest = Unpooled.compositeBuffer();
            String responseString = JSON.toJSONString(response);
            agentRequest.addComponents(true, Unpooled.copyInt(responseString.length()),
                    Unpooled.copiedBuffer(responseString, StandardCharsets.UTF_8));
            ctx.writeAndFlush(agentRequest);
        } catch (Exception e) {
            log.error("invoke error!", e);
        }
    }
}

