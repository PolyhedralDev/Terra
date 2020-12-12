package com.dfsek.terra.fabric;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.gaea.lang.Language;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.inventory.ItemHandle;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.config.base.PluginConfig;
import com.dfsek.terra.registry.ConfigRegistry;

import java.io.File;
import java.util.logging.Logger;

public class TerraFabricPlugin implements TerraPlugin {
    @Override
    public WorldHandle getWorldHandle() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return null;
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public PluginConfig getTerraConfig() {
        return null;
    }

    @Override
    public File getDataFolder() {
        return null;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public Language getLanguage() {
        return null;
    }

    @Override
    public ConfigRegistry getRegistry() {
        return null;
    }

    @Override
    public void reload() {

    }

    @Override
    public ItemHandle getItemHandle() {
        return null;
    }

    @Override
    public void register(TypeRegistry registry) {

    }
}
