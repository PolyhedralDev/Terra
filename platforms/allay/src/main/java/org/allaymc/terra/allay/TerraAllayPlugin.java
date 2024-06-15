package org.allaymc.terra.allay;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;

import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.world.generator.WorldGeneratorFactory;
import org.allaymc.terra.allay.generator.AllayGeneratorWrapper;


/**
 * Terra Project 2024/6/15
 *
 * @author daoge_cmd
 */
@Slf4j
public class TerraAllayPlugin extends Plugin {

    public static TerraAllayPlugin INSTANCE;
    public static AllayPlatform PLATFORM;

    {
        INSTANCE = this;
    }

    // TODO: Adapt command manager
    @Override
    public void onLoad() {
        log.info("Starting Terra...");

        log.info("Loading mapping...");
        Mapping.init();

        log.info("Initializing allay platform...");
        PLATFORM = new AllayPlatform();
        PLATFORM.getEventManager().callEvent(new PlatformInitializationEvent());

        log.info("Registering generator...");
        WorldGeneratorFactory.getFactory().register("TERRA", AllayGeneratorWrapper::new);

        log.info("Terra started");
    }
}
