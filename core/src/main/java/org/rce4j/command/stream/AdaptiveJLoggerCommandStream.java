package org.rce4j.command.stream;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * A {@link JLoggerCommandStream} that automatically adapts to hold all {@link Logger} instances in use and captures all of their output.
 * <p>
 * Does not capture all console output, only that from Java loggers.
 *
 * @see JLoggerCommandStream
 * @see CommandStream
 * @see Logger
 */
public class AdaptiveJLoggerCommandStream extends JLoggerCommandStream {
    public AdaptiveJLoggerCommandStream() {
        updateLoggers();
    }
    
    private final Collection<Logger> handledLoggers = new HashSet<>();
    
    @Override
    protected void onClose() {
        for (Iterator<Logger> it = handledLoggers.iterator(); it.hasNext(); ) {
            Logger logger = it.next();
            logger.removeHandler(getHandler());
            
            it.remove();
        }
    }
    
    private void updateLoggers() {
        for (Iterator<String> it = LogManager.getLogManager()
                .getLoggerNames()
                .asIterator(); it.hasNext(); ) {
            String loggerName = it.next();
            Logger logger = Logger.getLogger(loggerName);
            
            if (handledLoggers.add(logger))
                logger.addHandler(getHandler());
        }
    }
}
