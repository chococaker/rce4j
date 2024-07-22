package org.rce4j.server;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.rce4j.module.ModulePath;
import org.rce4j.net.WsConnection;

import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

class NettyWsConnection implements WsConnection {
    public NettyWsConnection(ModulePath path, SocketChannel channel) {
        this.channel = channel;
        this.path = path;
    }
    
    private final SocketChannel channel;
    private final ModulePath path;
    
    @Override
    public String getId() {
        return new String(channel.remoteAddress().getAddress().getAddress());
    }
    
    @Override
    public ModulePath getPath() {
        return path;
    }
    
    @Override
    public InetAddress getClient() {
        return channel.remoteAddress().getAddress();
    }
    
    @Override
    public void close() {
        try {
            channel.close().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void send(String s) {
        channel.writeAndFlush(new TextWebSocketFrame(s));
    }
}
