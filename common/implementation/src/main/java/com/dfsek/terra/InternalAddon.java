package com.dfsek.terra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;


@Addon("terra")
@Author("Terra")
@Version("1.0.0")
public class InternalAddon extends TerraAddon {
    private final AbstractPlatform main;
    private static final Logger logger = LoggerFactory.getLogger(InternalAddon.class);
    
    public InternalAddon(AbstractPlatform main) {
        this.main = main;
    }
    
    @Override
    public void initialize() {
        main.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(this, PlatformInitializationEvent.class)
            .then(event -> {
                logger.info("Loading config packs...");
                main.getRawConfigRegistry().loadAll(main);
                logger.info("Loaded packs.");
            })
            .global();
    }
}
