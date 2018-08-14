package com.tracy.trpc.protocol.netty.client;

import com.tracy.trpc.protocol.netty.server.NettyChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author tracy.
 * @create 2018-08-13 14:58
 **/
public class NettyClient {

    private EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors()) : new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

    private Channel connect(String host, int port) {
        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2))
                                .addLast(new LengthFieldPrepender(2))
                                .addLast(new NettyChannelHandler());
                    }
                });
        ChannelFuture f = b.connect(host, port);
        return f.channel();
    }
}
