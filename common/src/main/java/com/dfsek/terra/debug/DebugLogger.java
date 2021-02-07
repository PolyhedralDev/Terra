package com.dfsek.terra.debug;

import java.util.logging.Logger;

public class DebugLogger {
    private final Logger logger;
    private boolean debug = false;

    public DebugLogger(Logger logger) {
        this.logger = logger;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void info(String message) {
        if(debug) logger.info(message);
    }

    public void warn(String message) {
        if(debug) logger.warning(message);
    }

    public void error(String message) {
        if(debug) logger.severe(message);
    }

    public void stack(Exception e) {
        if(debug) e.printStackTrace();
    }
}
