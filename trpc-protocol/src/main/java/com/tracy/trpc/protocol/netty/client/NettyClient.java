package com.tracy.trpc.protocol.netty.client;

import com.tracy.trpc.protocol.netty.server.NettyChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

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
    public Channel connect(String host, int port) throws InterruptedException {
        EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new NettyChannelInitializer());
        ChannelFuture f = b.connect(host, port);
        return f.channel();
    }
}
