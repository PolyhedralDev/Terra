package com.dfsek.terra.cli;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class TerraCLI {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerraCLI.class);
    
    public static void main(String... args) {
        LOGGER.info("Starting Terra CLI...");
        
        CLIPlatform platform = new CLIPlatform();
        platform.getEventManager().callEvent(new PlatformInitializationEvent());
    }
}
