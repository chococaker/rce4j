package org.rce4j.io;

import java.io.IOException;

public class ClosedStreamException extends IOException {
    public ClosedStreamException() {
    }
    
    public ClosedStreamException(String reason) {
        super(reason);
    }
}
