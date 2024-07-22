package org.rce4j.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.rce4j.RceServer;
import org.rce4j.module.HttpModuleSuite;
import org.rce4j.module.WsModuleSuite;

public class NettyRceServer extends RceServer {
    public NettyRceServer(int port, HttpModuleSuite httpModules) {
        super(port, httpModules);
    }
    
    public NettyRceServer(int port, WsModuleSuite wsModules) {
        super(port, wsModules);
    }
    
    public NettyRceServer(int port, HttpModuleSuite httpModules, WsModuleSuite wsModules) {
        super(port, httpModules, wsModules);
    }
    
    public NettyRceServer(int port, WsModuleSuite wsModules, HttpModuleSuite httpModules) {
        super(port, wsModules, httpModules);
    }
    
    private ServerBootstrap internalServer;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    
    @Override
    public void open() {
        new Thread(() -> {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            try {
                internalServer = new ServerBootstrap();
                internalServer.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new HttpInitialiser(this))
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                
                ChannelFuture f = internalServer.bind(getPort()).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }).start();
    }
    
    @Override
    public void close() {
        if (internalServer != null) {
            try {
                bossGroup.shutdownGracefully().sync();
                workerGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
