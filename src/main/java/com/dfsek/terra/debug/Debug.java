package com.dfsek.terra.debug;

import com.dfsek.terra.config.base.PluginConfig;

import java.util.logging.Logger;

public class Debug {
    public static Logger logger;

    public static void setLogger(Logger logger) {
        Debug.logger = logger;
    }

    public static void info(String message) {
        if(PluginConfig.isDebug()) logger.info(message);
    }

    public static void warn(String message) {
        if(PluginConfig.isDebug()) logger.warning(message);
    }

    public static void error(String message) {
        if(PluginConfig.isDebug()) logger.severe(message);
    }

    public static void stack(Exception e) {
        if(PluginConfig.isDebug()) e.printStackTrace();
    }
}
