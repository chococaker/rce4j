package org.rce4j.command.stream;

import java.util.logging.Logger;

public class SingleJLoggerCommandStream extends JLoggerCommandStream {
    public SingleJLoggerCommandStream(Logger logger) {
        this.logger = logger;
        logger.addHandler(getHandler());
    }
    
    private final Logger logger;
    
    @Override
    protected void onClose() {
        logger.removeHandler(getHandler());
    }
}
