package com.dfsek.terra.allay;

import org.allaymc.api.eventbus.EventHandler;
import org.allaymc.api.eventbus.event.world.WorldUnloadEvent;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.registry.Registries;
import org.allaymc.api.server.Server;

import com.dfsek.terra.allay.generator.AllayGeneratorWrapper;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;


/**
 * @author daoge_cmd
 */
public class TerraAllayPlugin extends Plugin {

    public static TerraAllayPlugin instance;
    public static AllayPlatform platform;

    {
        TerraAllayPlugin.instance = this;
    }

    @Override
    public void onLoad() {
        this.pluginLogger.info("Starting Terra...");

        this.pluginLogger.info("Loading mapping...");
        Mapping.init();

        this.pluginLogger.info("Initializing allay platform...");
        TerraAllayPlugin.platform = new AllayPlatform();
        TerraAllayPlugin.platform.getEventManager().callEvent(new PlatformInitializationEvent());
        // TODO: adapt command manager

        this.pluginLogger.info("Registering generator...");
        Registries.WORLD_GENERATOR_FACTORIES.register("TERRA", preset -> {
            try {
                AllayGeneratorWrapper wrapper = new AllayGeneratorWrapper(preset);
                AllayPlatform.GENERATOR_WRAPPERS.add(wrapper);
                return wrapper.getAllayWorldGenerator();
            } catch(IllegalArgumentException e) {
                TerraAllayPlugin.instance.getPluginLogger().error("Fail to create world generator with preset: {}", preset, e);
                return Registries.WORLD_GENERATOR_FACTORIES.get("FLAT").apply("");
            }
        });

        this.pluginLogger.info("Terra started");
    }

    @Override
    public void onEnable() {
        Server.getInstance().getEventBus().registerListener(this);
    }

    @Override
    public boolean isReloadable() {
        return true;
    }

    @Override
    public void reload() {
        if(TerraAllayPlugin.platform.reload()) {
            this.pluginLogger.info("Terra reloaded successfully.");
        } else {
            this.pluginLogger.error("Terra failed to reload.");
        }
    }

    @EventHandler
    private void onWorldUnload(WorldUnloadEvent event) {
        AllayPlatform.GENERATOR_WRAPPERS.removeIf(
            wrapper -> wrapper.getAllayWorldGenerator().getDimension().getWorld() == event.getWorld());
    }
}
