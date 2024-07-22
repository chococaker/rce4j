package org.rce4j.module.event;

import net.chococaker.jjason.JsonObject;
import org.rce4j.net.HttpCode;
import org.rce4j.net.RequestType;
import org.rce4j.net.UploadedFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public non-sealed interface BackdoorHttpEvent extends BackdoorEvent {
    RequestType getRequestType();
    
    boolean hasCompleted();
    
    
    List<UploadedFile> getUploadedFiles();
    
    
    void respond(String s);
    
    void respond(InputStream inputStream);
    
    void respondJson(String s);
    
    default void respondJson(JsonObject json) {
        respondJson(json.toString());
    }
    
    default void status(HttpCode code) {
        status(code.CODE, code.MESSAGE);
    }
    
    default void status(int status) {
        status(status, HttpCode.valueOf(status).MESSAGE);
    }
    
    void status(int status, String message);
    
    
    Map<String, List<String>> getQueryParams();
    
    default List<String> getQueryParams(String s) {
        return getQueryParams().get(s);
    }
    
    default String getQueryParam(String s) {
        List<String> params = getQueryParams(s);
        
        return params == null || params.isEmpty() ? null : params.get(0);
    }
}
