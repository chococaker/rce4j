package org.rce4j.exception;

import org.rce4j.net.HttpCode;

public class HttpException extends RuntimeException {
    public HttpException(int code) {
        this(HttpCode.valueOf(code));
    }
    
    public HttpException(int code, String message) {
        this(HttpCode.valueOf(code), message);
    }
    
    public HttpException(int code, String message, Throwable cause) {
        this(HttpCode.valueOf(code), message, cause);
    }
    
    public HttpException(int code, Throwable cause) {
        this(HttpCode.valueOf(code), cause);
    }
    
    public HttpException(HttpCode code) {
        super(code.MESSAGE);
        this.code = code.CODE;
    }
    
    public HttpException(HttpCode code, String message) {
        super(message);
        this.code = code.CODE;
    }
    
    public HttpException(HttpCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code.CODE;
    }
    
    public HttpException(HttpCode code, Throwable cause) {
        super(cause);
        this.code = code.CODE;
    }
    
    private final int code;
    
    public int getCode() {
        return code;
    }
    
    public HttpCode getHttpCode() {
        return HttpCode.valueOf(code);
    }
}
