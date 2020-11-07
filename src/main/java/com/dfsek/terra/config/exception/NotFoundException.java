package com.dfsek.terra.config.exception;

/**
 * Thrown when a required config item is not found.
 */
public class NotFoundException extends ConfigException {
    private static final long serialVersionUID = -3763471113262357426L;

    public NotFoundException(String item, String itemName, String id) {
        super(item + " \"" + itemName + "\" cannot be found!", id);
    }
}
