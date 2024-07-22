package org.rce4j.command.stream;

import org.rce4j.command.handler.CommandStreamHandler;
import org.rce4j.io.ClosedStreamException;
import org.rce4j.util.ConcurrentHashSet;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A {@link CommandStream} that captures the output of {@link Logger} instances defined by all if its children.
 *
 * @see CommandStream
 */
public abstract class JLoggerCommandStream implements CommandStream {
    public static final Collection<Level> RECORDED_LEVELS = Set.of(Level.INFO, Level.WARNING, Level.SEVERE);
    
    private final Collection<CommandStreamHandler> handlers = new ConcurrentHashSet<>();
    
    private String line;
    private JavaLoggingHandler loggingHandler = new JavaLoggingHandler();
    
    private boolean isOpen = false;
    
    private final Object awaitingObject = new Object();
    
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
    public final void close() {
        isOpen = false;
        onClose();
        loggingHandler = null;
    }
    
    protected void onClose() {
    }
    
    @Override
    public void addHandler(CommandStreamHandler handler) {
        handlers.add(handler);
    }
    
    protected final Handler getHandler() {
        return loggingHandler;
    }
    
    protected class JavaLoggingHandler extends Handler {
        @Override
        public void publish(LogRecord record) {
            if (isOpen && isRecordedLevel(record.getLevel())) {
                line = record.getMessage();
                for (CommandStreamHandler handler : handlers) {
                    try {
                        handler.handle(line);
                    } catch (RuntimeException e) {
                        for (CommandStreamHandler errorHandler : handlers) {
                            errorHandler.onError(handler, e);
                        }
                    }
                }
                
                synchronized (awaitingObject) {
                    super.notifyAll();
                }
            }
        }
        
        @Override
        public void flush() {
        }
        
        @Override
        public void close() {
        }
        
        private boolean isRecordedLevel(Level level) {
            return RECORDED_LEVELS.contains(level);
        }
    }
}
