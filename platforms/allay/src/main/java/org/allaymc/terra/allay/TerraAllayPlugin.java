package org.allaymc.terra.allay;

import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.registry.Registries;
import org.allaymc.terra.allay.generator.AllayGeneratorWrapper;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;

/**
 * @author daoge_cmd
 */
public class TerraAllayPlugin extends Plugin {

    public static TerraAllayPlugin INSTANCE;
    public static AllayPlatform PLATFORM;

    {
        INSTANCE = this;
    }

    // TODO: Adapt command manager
    @Override
    public void onLoad() {
        pluginLogger.info("Starting Terra...");

        pluginLogger.info("Loading mapping...");
        Mapping.init();

        pluginLogger.info("Initializing allay platform...");
        PLATFORM = new AllayPlatform();
        PLATFORM.getEventManager().callEvent(new PlatformInitializationEvent());

        pluginLogger.info("Registering generator...");
        Registries.WORLD_GENERATOR_FACTORIES.register("TERRA", preset -> new AllayGeneratorWrapper(preset).getAllayWorldGenerator());

        pluginLogger.info("Terra started");
    }
}
