package com.dfsek.terra.debug;

import java.util.logging.Logger;

public class Debug {
    private static Logger logger;
    private static boolean debug = false;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Debug.debug = debug;
    }

    public static void setLogger(Logger logger) {
        Debug.logger = logger;
    }

    public static void info(String message) {
        if(debug) logger.info(message);
    }

    public static void warn(String message) {
        if(debug) logger.warning(message);
    }

    public static void error(String message) {
        if(debug) logger.severe(message);
    }

    public static void stack(Exception e) {
        if(debug) e.printStackTrace();
    }
}
