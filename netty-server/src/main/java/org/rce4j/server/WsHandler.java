package org.rce4j.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.chococaker.jjason.JsonObject;
import net.chococaker.jjason.reader.JsonReader;
import org.rce4j.RceServer;
import org.rce4j.module.ModuleManager;
import org.rce4j.module.ModulePath;
import org.rce4j.module.event.WsEventType;

class WsHandler extends ChannelInboundHandlerAdapter {
    public WsHandler(RceServer server, ModulePath path, SocketChannel socket) {
        this.server = server;
        this.moduleManager = server.getModuleManager();
        
        this.connection = new NettyWsConnection(path, socket);
    }
    
    private final RceServer server;
    private final ModuleManager moduleManager;
    
    private final NettyWsConnection connection;
    
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        if (msg instanceof TextWebSocketFrame frame) {
            String s = frame.text();
            JsonObject json = JsonReader.objectReader(s).read();;
            
            moduleManager.fire(new NettyWsEvent(WsEventType.MESSAGE, server, connection, context, json));
        } else if (msg instanceof CloseWebSocketFrame) {
            moduleManager.fire(new NettyWsEvent(WsEventType.CLOSE, server, connection, context, null));
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        moduleManager.fire(new NettyWsEvent(WsEventType.CLOSE, server, connection, ctx, null));
    }
}
