package com.tracy.trpc.protocol.netty;

import com.tracy.trpc.common.model.TConfig;
import com.tracy.trpc.common.model.TProtocol;
import com.tracy.trpc.protocol.action.AbstractProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by lurenjie on 2017/6/12
 */
public class NettyProtocol extends AbstractProtocol {
    /**
     * boss accepts an incoming connection
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     * handles the traffic of the accepted connection once the boss accepts the connection and registers the accepted connection to the worker
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyProtocol() {
        super(TProtocol.NETTY);
    }

    @Override
    public Runnable startService(TConfig config) throws Exception {
        //ServerBootstrap is a helper class that sets up a server
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                //instantiate a new Channel to accept incoming connections.
                .channel(NioServerSocketChannel.class)
                // a special handler that is purposed to help a user configure a new Channel.
                //adding some handlers to implement your network application
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyChannelHandler());
                    }
                })
                //@link http://netty.io/4.0/api/io/netty/channel/ChannelOption.html
                //@link http://netty.io/4.0/api/io/netty/channel/ChannelConfig.html
                //option() is for the NioServerSocketChannel that accepts incoming connections
                //SO_BACKLOG--cache the ServerSocket connect which can not fetch,set the max value 128
                .option(ChannelOption.SO_BACKLOG, 128)
                //childOption() is for the Channels accepted by the parent ServerChannel
                //set the TCP/IP connect stay ESTABLISHED status when session is finish
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        ChannelFuture f = bootstrap.bind(config.getServicePort()).sync();
        // Wait until the server socket is closed.
        f.channel().closeFuture().sync();
        return new StopServer(bossGroup, workerGroup);
    }

    private class StopServer implements Runnable {
        private EventLoopGroup bossGroup;
        private EventLoopGroup workerGroup;

        StopServer(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
            this.bossGroup = bossGroup;
            this.workerGroup = workerGroup;
        }

        @Override
        public void run() {
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
        }
    }
}
