package com.dfsek.terra.api.core;

import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.language.Language;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.debug.DebugLogger;
import com.dfsek.terra.registry.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;

import java.io.File;
import java.util.logging.Logger;

public interface TerraPlugin extends LoaderRegistrar {
    WorldHandle getWorldHandle();

    boolean isEnabled();

    TerraWorld getWorld(World world);

    Logger getLogger();

    PluginConfig getTerraConfig();

    File getDataFolder();

    boolean isDebug();

    Language getLanguage();

    ConfigRegistry getRegistry();

    void reload();

    ItemHandle getItemHandle();

    void saveDefaultConfig();

    String platformName();

    default void packPreLoadCallback(ConfigPack pack) {

    }

    default void packPostLoadCallback(ConfigPack pack) {

    }

    DebugLogger getDebugLogger();
}
