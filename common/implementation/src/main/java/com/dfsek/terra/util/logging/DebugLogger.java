package com.dfsek.terra.util.logging;

import com.dfsek.terra.api.Logger;


public class DebugLogger implements Logger {
    private final Logger logger;
    private boolean debug = false;
    
    public DebugLogger(Logger logger) {
        this.logger = logger;
    }
    
    public void info(String message) {
        if(debug) logger.info(message);
    }
    
    public void warning(String message) {
        if(debug) logger.warning(message);
    }
    
    public void severe(String message) {
        if(debug) logger.severe(message);
    }
    
    public void stack(Throwable e) {
        if(debug) e.printStackTrace();
    }
    
    public boolean isDebug() {
        return debug;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
