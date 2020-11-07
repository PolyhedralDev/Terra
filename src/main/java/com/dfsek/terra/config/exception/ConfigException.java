package com.dfsek.terra.config.exception;

import org.bukkit.configuration.InvalidConfigurationException;

/**
 * Thrown when a config item is not valid.
 */
public class ConfigException extends InvalidConfigurationException {
    private static final long serialVersionUID = -4342864317005935979L;
    private final String message;
    private final String id;

    public ConfigException(String message, String id) {
        this.message = message;
        this.id = id;
    }

    @Override
    public String getMessage() {
        String ex = getStackTrace()[0].getClassName();
        return "Configuration error for " + ex.substring(ex.lastIndexOf(".") + 1) + " with ID \"" + id + "\": \n\n" + message;
    }
}
