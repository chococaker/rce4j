package org.rce4j.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import org.rce4j.RceServer;

class HttpInitialiser extends ChannelInitializer<SocketChannel> {
    public HttpInitialiser(RceServer server) {
        this.server = server;
    }
    
    private final RceServer server;
    
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        pipeline.addLast("httpHandler", new HttpHandler(server));
    }
}
