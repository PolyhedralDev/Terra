package com.dfsek.terra.api.platform;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.gaea.lang.Language;
import com.dfsek.terra.api.platform.inventory.ItemHandle;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.WorldHandle;
import com.dfsek.terra.config.base.PluginConfig;
import com.dfsek.terra.registry.ConfigRegistry;

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
}
