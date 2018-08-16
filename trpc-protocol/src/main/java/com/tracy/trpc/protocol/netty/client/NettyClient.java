package com.tracy.trpc.protocol.netty.client;

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

    /**
     * 连接远程服务
     *
     * @param host 远程地址
     * @param port 远程端口
     * @return 通信端口
     */
    public Channel connect(String host, int port) {
        EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
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
                                .addLast(new NettyClientChannelInitializer());
                    }
                });
        ChannelFuture f = b.connect(host, port);
        return f.channel();
    }
}
