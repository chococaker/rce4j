package org.rce4j.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.rce4j.exception.UnexpectedException;
import org.rce4j.module.ModulePath;
import org.rce4j.net.RequestType;

import java.net.URI;
import java.net.URISyntaxException;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

final class NettyUtil {
    public static ModulePath getModulePath(HttpRequest request) {
        String uriString = request.uri().split("\\?")[0];
        
        URI uri;
        try {
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            throw new UnexpectedException(e);
        }
        
        return new ModulePath(uri);
    }
    
    public static HttpMethod adapt(RequestType requestType) {
        try {
            return HttpMethod.valueOf(requestType.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(RequestType.class.getName() + "." + requestType + " string value not found in " + HttpMethod.class.getName(), e);
        }
    }
    
    public static RequestType adapt(HttpMethod handlerType) {
        try {
            return RequestType.valueOf(handlerType.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(HttpMethod.class.getName() + "." + handlerType + " string value not found in " + RequestType.class.getName(), e);
        }
    }
    
    public static void sendCode(ChannelHandlerContext ctx, int code, String message) {
        HttpResponseStatus status = HttpResponseStatus.valueOf(code);
        
        FullHttpResponse response;
        if (message == null) {
            response = new DefaultFullHttpResponse(HTTP_1_1, status);
        } else {
            ByteBuf buffer = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
            response = new DefaultFullHttpResponse(HTTP_1_1, status, buffer);
        }
        
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
