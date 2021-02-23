package com.dfsek.terra.api;

import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.api.util.logging.Logger;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.TerraWorld;

import java.io.File;

/**
 * Represents a Terra mod/plugin instance.
 */
public interface TerraPlugin extends LoaderRegistrar {
    WorldHandle getWorldHandle();

    TerraWorld getWorld(World world);

    Logger logger();

    PluginConfig getTerraConfig();

    File getDataFolder();

    boolean isDebug();

    Language getLanguage();

    CheckedRegistry<ConfigPack> getConfigRegistry();

    LockedRegistry<TerraAddon> getAddons();

    boolean reload();

    ItemHandle getItemHandle();

    void saveDefaultConfig();

    String platformName();

    DebugLogger getDebugLogger();

    EventManager getEventManager();
}
