package org.rce4j.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import net.chococaker.jjason.JsonObject;
import net.chococaker.jjason.reader.JsonReader;
import org.rce4j.RceServer;
import org.rce4j.module.ModulePath;
import org.rce4j.module.event.BackdoorHttpEvent;
import org.rce4j.net.RequestType;
import org.rce4j.net.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpEvent implements BackdoorHttpEvent {
    public NettyHttpEvent(RceServer server, RequestType requestType, String uri, ModulePath path, HttpContent content, ChannelHandlerContext ctx, List<UploadedFile> uploadedFiles) {
        this.server = server;
        this.requestType = requestType;
        this.context = ctx;
        this.modulePath = path;
        
        if (requestType == RequestType.GET) { // TODO: tempfix -- make sure to detect when it is an EmptyByteBufBE
            this.body = null;
        } else {
            this.body = JsonReader.objectReader(content.content().toString()).read();
        }
        
        this.params = new QueryStringDecoder(uri).parameters();
        this.uploadedFiles = uploadedFiles;
    }
    
    private final ModulePath modulePath;
    private final RequestType requestType;
    private final RceServer server;
    private final JsonObject body;
    private final Map<String, List<String>> params;
    private final List<UploadedFile> uploadedFiles;
    
    private final ChannelHandlerContext context;
    
    private boolean hasCompleted;
    
    @Override
    public ModulePath getPath() {
        return modulePath;
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
    public InetAddress getClient() { // TODO
        return null;
    }
    
    @Override
    public RequestType getRequestType() {
        return requestType;
    }
    
    @Override
    public boolean hasCompleted() {
        return hasCompleted;
    }
    
    @Override
    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }
    
    @Override
    public void respond(String s) {
        complete();
        
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(s, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    @Override
    public void respond(InputStream inputStream) {
        complete();
        
        try (inputStream) {
            ByteBuf buf = Unpooled.copiedBuffer(inputStream.readAllBytes());
            
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, buf);
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
            
            context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void respondJson(String s) {
        complete();
        
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(s, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        
        context.write(response);
        context.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }
    
    @Override
    public void status(int status, String message) {
        complete();
        
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(status, message));
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    @Override
    public Map<String, List<String>> getQueryParams() {
        return params;
    }
    
    private void complete() {
        if (hasCompleted)
            throw new IllegalStateException("event has already completed");
        
        hasCompleted = true;
    }
}
