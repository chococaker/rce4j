package org.rce4j.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.rce4j.RceServer;
import org.rce4j.exception.HttpException;
import org.rce4j.module.ModulePath;
import org.rce4j.module.event.WsEventType;
import org.rce4j.net.RequestType;
import org.rce4j.net.UploadedFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.rce4j.server.NettyUtil.getModulePath;
import static org.rce4j.server.NettyUtil.sendCode;

class HttpHandler extends ChannelInboundHandlerAdapter {
    public HttpHandler(RceServer server) {
        this.server = server;
    }
    
    private final RceServer server;
    
    private String uri;
    private RequestType requestType;
    private ModulePath path;
    
    private HttpPostRequestDecoder httpDecoder;
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest request)
            handle(ctx, request);
        else if (msg instanceof HttpContent content)
            handle(ctx, content);
    }
    
    private void handle(ChannelHandlerContext ctx, HttpRequest request) {
        HttpHeaders headers = request.headers();
        
        if ("Upgrade".equalsIgnoreCase(headers.get(HttpHeaderNames.CONNECTION)) &&
                "WebSocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {
            
            ModulePath modulePath = getModulePath(request);
            
            ctx.pipeline().replace(this, "websocketHandler",
                    new WsHandler(server, modulePath, (SocketChannel) ctx.channel()));
            server.getModuleManager().fire(new NettyWsEvent(WsEventType.CONNECT, server, modulePath, ctx, null));
            
            WebSocketServerHandshakerFactory wsFactory =
                    new WebSocketServerHandshakerFactory(toWebSocketURL(request),
                            null, true);
            WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
            
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), request);
            }
            
            return;
        }
        
        requestType = NettyUtil.adapt(request.method());
        path = getModulePath(request);
        uri = request.uri();
        
        if (request.method() == HttpMethod.POST) {
            HttpDataFactory factory = new DefaultHttpDataFactory();
            httpDecoder = new HttpPostRequestDecoder(factory, request);
            httpDecoder.setDiscardThreshold(0);
        }
    }
    
    private void handle(ChannelHandlerContext ctx, HttpContent content) {
        List<UploadedFile> uploadedFiles = List.of();
        
        if (httpDecoder != null) {
            httpDecoder.offer(content);
            
            try {
                uploadedFiles = getUploadedFiles();
            } finally {
                if (content instanceof LastHttpContent) {
                    httpDecoder.destroy();
                    httpDecoder = null;
                }
            }
        }
        
        try {
            server.getModuleManager().fire(new NettyHttpEvent(server, requestType, uri, path, content, ctx, uploadedFiles));
        } catch (HttpException e) {
            sendCode(ctx, e.getCode(), e.getMessage());
        }
    }
    
    private List<UploadedFile> getUploadedFiles() {
        List<UploadedFile> uploadedFiles = new LinkedList<>();
        
        while (httpDecoder.hasNext()) {
            InterfaceHttpData data = httpDecoder.next();
            if (data != null && data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                try {
                    FileUpload fileUpload = (FileUpload) data;
                    uploadedFiles.add(new UploadedFile() {
                        @Override
                        public String getName() {
                            return fileUpload.getName();
                        }
                        
                        @Override
                        public InputStream getInputStream() throws IOException {
                            return new FileInputStream(fileUpload.getFile());
                        }
                    });
                } finally {
                    data.release();
                }
            }
        }
        
        return Collections.unmodifiableList(uploadedFiles);
    }
    
    private static String toWebSocketURL(HttpRequest req) {
        return "ws://" + req.headers().get("Host") + req.uri();
    }
}
