package com.dfsek.terra.config.exception;

/**
 * Thrown when a required config item is not found.
 */
public class NotFoundException extends ConfigException {
    public NotFoundException(String item, String itemName, String id) {
        super(item + " \"" + itemName + "\" cannot be found!", id);
    }
}
