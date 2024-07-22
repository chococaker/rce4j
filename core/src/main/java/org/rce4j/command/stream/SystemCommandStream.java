package org.rce4j.command.stream;

import org.rce4j.command.handler.CommandStreamHandler;
import org.rce4j.exception.UnexpectedException;
import org.rce4j.io.ClosedStreamException;
import org.rce4j.util.ConcurrentHashSet;

import java.io.PrintStream;
import java.util.Collection;

public class SystemCommandStream implements CommandStream {
    public SystemCommandStream() {
        capturedOut = new CapturedPrintStream(System.out);
        capturedErr = new CapturedPrintStream(System.err);
        
        try {
            updateSystemStreams();
        } catch (ClosedStreamException e) {
            throw new UnexpectedException(e);
        }
        
        new Thread(() -> {
            while (capturedOut != null) { // while is not closed
                PrintStream currentOut = System.out;
                PrintStream currentErr = System.err;
                
                boolean updated = false;
                
                if (currentOut != capturedOut) {
                    capturedOut = new CapturedPrintStream(currentOut);
                    updated = true;
                }
                
                if (currentErr != capturedErr) {
                    capturedErr = new CapturedPrintStream(currentErr);
                    updated = true;
                }
                
                if (updated) {
                    try {
                        updateSystemStreams();
                    } catch (ClosedStreamException e) {
                        throw new UnexpectedException(e);
                    }
                }
            }
        }).start();
    }
    
    private String line;
    
    private CapturedPrintStream capturedOut;
    private CapturedPrintStream capturedErr;
    
    private final Object awaitingObject = new Object();
    
    private final Collection<CommandStreamHandler> handlers = new ConcurrentHashSet<>();
    
    @Override
    public String nextLine() throws ClosedStreamException {
        try {
            synchronized (awaitingObject) {
                awaitingObject.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        
        if (line == null)
            throw new ClosedStreamException();
        return line;
    }
    
    @Override
    public void close() {
        if (capturedOut == null || capturedErr == null)
            return;
        
        System.setOut(capturedOut.originalStream);
        System.setErr(capturedErr.originalStream);
        
        capturedOut = null;
        capturedErr = null;
    }
    
    @Override
    public void addHandler(CommandStreamHandler handler) {
        handlers.add(handler);
    }
    
    public boolean isClosed() {
        return capturedOut == null;
    }
    
    private void updateSystemStreams() throws ClosedStreamException {
        if (capturedOut == null || capturedErr == null)
            throw new ClosedStreamException();
        
        System.setOut(capturedOut);
        System.setErr(capturedErr);
    }
    
    private class CapturedPrintStream extends PrintStream {
        public CapturedPrintStream(PrintStream originalStream) {
            super(originalStream);
            this.originalStream = originalStream;
        }
        
        private final PrintStream originalStream;
        
        @Override
        public void write(byte[] b, int off, int len) {
                line = new String(b, off, len);
                
                for (CommandStreamHandler handler : handlers) {
                    handler.handle(line);
                }
                
                synchronized (awaitingObject) {
                    awaitingObject.notifyAll();
                }
                
                originalStream.write(b, off, len);
        }
        
        @Override
        public void writeBytes(byte[] b) {
            write(b, 0, b.length);
        }
        
        @Override
        public void write(byte[] b) {
            write(b, 0, b.length);
        }
    }
}
