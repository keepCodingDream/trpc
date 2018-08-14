package com.tracy.trpc.protocol.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tracy.
 * @create 2018-08-10 11:23
 **/
@Slf4j
public class NettyServer {

    private EventLoopGroup bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors()) : new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

    private static ServerBootstrap bootstrap;


    public void startServer(int port) {
        if (bootstrap == null) {
            synchronized (NettyServer.class) {
                if (bootstrap == null) {
                    try {
                        bootstrap = new ServerBootstrap()
                                .group(bossGroup, workerGroup)
                                .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                                .childHandler(new NettyChannelInitializer())
                                .childOption(ChannelOption.SO_KEEPALIVE, true)
                                .childOption(ChannelOption.TCP_NODELAY, true);
                        Channel channel = bootstrap.bind(port).sync().channel();
                        channel.closeFuture().sync();
                        log.info("TRPC start successfully!");
                    } catch (Exception e) {
                        log.error("TRPC start server error will exit", e);
                        throw new RuntimeException(e);
                    } finally {
                        workerGroup.shutdownGracefully();
                        bossGroup.shutdownGracefully();
                    }
                } else {
                    log.warn("TRPC Netty server started already");
                }
            }
        } else {
            log.warn("TRPC Netty server started already");
        }
    }

}
