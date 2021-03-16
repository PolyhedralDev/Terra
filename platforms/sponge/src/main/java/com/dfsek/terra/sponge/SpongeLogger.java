package com.dfsek.terra.sponge;

import com.dfsek.terra.api.util.logging.Logger;

public class SpongeLogger implements Logger {
    private final org.slf4j.Logger logger;

    public SpongeLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warning(String message) {
        logger.warn(message);
    }

    @Override
    public void severe(String message) {
        logger.error(message);
    }
}
