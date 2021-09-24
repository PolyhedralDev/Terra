package com.dfsek.terra.util.logging;

import com.dfsek.terra.api.util.Logger;


public class JavaLogger implements Logger {
    private final java.util.logging.Logger logger;
    
    public JavaLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public void info(String message) {
        logger.info(message);
    }
    
    @Override
    public void warning(String message) {
        logger.warning(message);
    }
    
    @Override
    public void severe(String message) {
        logger.severe(message);
    }
}
