package org.rce4j.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import net.chococaker.jjason.JsonObject;
import org.rce4j.RceServer;
import org.rce4j.module.ModulePath;
import org.rce4j.module.event.BackdoorWsEvent;
import org.rce4j.module.event.WsEventType;
import org.rce4j.net.WsConnection;

import java.net.InetAddress;

class NettyWsEvent implements BackdoorWsEvent {
    public NettyWsEvent(WsEventType type,
                        RceServer server,
                        ModulePath path,
                        ChannelHandlerContext context,
                        JsonObject message) {
        this(type, server, new NettyWsConnection(path, (SocketChannel) context.channel()), context, message);
    }
    
    public NettyWsEvent(WsEventType type,
                        RceServer server,
                        WsConnection websocket,
                        ChannelHandlerContext context,
                        JsonObject message) {
        this.type = type;
        this.server = server;
        this.websocket = websocket;
        this.body = message;
    }
    
    private final WsEventType type;
    private final WsConnection websocket;
    private final RceServer server;
    private final JsonObject body;
    
    @Override
    public ModulePath getPath() {
        return websocket.getPath();
    }
    
    @Override
    public JsonObject getBody() {
        return body;
    }
    
    @Override
    public RceServer getServer() {
        return server;
    }
    
    @Override
    public InetAddress getClient() {
        return null;
    }
    
    @Override
    public WsEventType getType() {
        return type;
    }
    
    @Override
    public WsConnection getConnection() {
        return websocket;
    }
}
