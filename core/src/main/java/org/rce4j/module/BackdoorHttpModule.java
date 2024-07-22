package org.rce4j.module;

import org.rce4j.exception.HttpException;
import org.rce4j.exception.UnexpectedException;
import org.rce4j.module.event.BackdoorHttpEvent;
import org.rce4j.net.HttpCode;
import org.rce4j.net.RequestType;
import org.rce4j.validator.JsonValidator;

import java.util.HashMap;
import java.util.Map;

public abstract class BackdoorHttpModule implements BackdoorModule<BackdoorHttpEvent> {
    public BackdoorHttpModule(String name, String path) {
        this(name, new ModulePath(path));
    }
    
    public BackdoorHttpModule(String name, ModulePath path) {
        this.name = name;
        this.path = path;
    }
    
    public BackdoorHttpModule(String name, String path, Map<RequestType, JsonValidator> validators) {
        this(name, new ModulePath(path), validators);
    }
    
    public BackdoorHttpModule(String name, ModulePath path, Map<RequestType, JsonValidator> validators) {
        this.name = name;
        this.path = path;
        
        this.validators.putAll(validators);
    }
    
    protected final String name;
    protected final ModulePath path;
    
    private final Map<RequestType, JsonValidator> validators = new HashMap<>();
    
    
    public void onGet(BackdoorHttpEvent event) throws HttpException {
        throw new HttpException(HttpCode.NOT_FOUND_404);
    }
    
    public void onHead(BackdoorHttpEvent event) throws HttpException {
        throw new HttpException(HttpCode.NOT_FOUND_404);
    }
    
    public void onPost(BackdoorHttpEvent event) throws HttpException {
        throw new HttpException(HttpCode.NOT_FOUND_404);
    }
    
    public void onPut(BackdoorHttpEvent event) throws HttpException {
        throw new HttpException(HttpCode.NOT_FOUND_404);
    }
    
    public void onDelete(BackdoorHttpEvent event) throws HttpException {
        throw new HttpException(HttpCode.NOT_FOUND_404);
    }
    
    public void onConnect(BackdoorHttpEvent event) throws HttpException {
        throw new HttpException(HttpCode.NOT_FOUND_404);
    }
    
    public void onOptions(BackdoorHttpEvent event) throws HttpException {
        throw new HttpException(HttpCode.NOT_FOUND_404);
    }
    
    public void onTrace(BackdoorHttpEvent event) throws HttpException {
        throw new HttpException(HttpCode.NOT_FOUND_404);
    }
    
    public void onPatch(BackdoorHttpEvent event) throws HttpException {
        throw new HttpException(HttpCode.NOT_FOUND_404);
    }
    
    
    public final void on(BackdoorHttpEvent event) throws HttpException {
        JsonValidator validator = validators.get(event.getRequestType());
        if (validator != null && !validator.test(event.getBody())) {
            throw new HttpException(HttpCode.BAD_REQUEST_400);
        }
        
        switch (event.getRequestType()) {
            case GET -> onGet(event);
            case HEAD -> onHead(event);
            case POST -> onPost(event);
            case PUT -> onPut(event);
            case DELETE -> onDelete(event);
            case CONNECT -> onConnect(event);
            case OPTIONS -> onOptions(event);
            case TRACE -> onTrace(event);
            case PATCH -> onPatch(event);
            
            default -> throw new UnexpectedException("Unknown request type: " + event.getRequestType());
        }
    }
    
    public final String getName() {
        return name;
    }
    
    public final ModulePath getPath() {
        return path;
    }
    
    protected void addValidator(RequestType requestType, JsonValidator validator) {
        if (requestType == RequestType.GET)
            throw new IllegalArgumentException("requestType may not be " + RequestType.GET);
        validators.put(requestType, validator);
    }
}
