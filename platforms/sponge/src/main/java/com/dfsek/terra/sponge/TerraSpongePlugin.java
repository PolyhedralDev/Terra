package com.dfsek.terra.sponge;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.gaea.lang.Language;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.inventory.ItemHandle;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.config.base.PluginConfig;
import com.dfsek.terra.registry.ConfigRegistry;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;
import java.io.File;
import java.util.logging.Logger;

@Plugin(id = "terra", name = "Terra", version = "@VERSION", description = "Terra world generator")
public class TerraSpongePlugin implements TerraPlugin {

    @Inject
    private Logger logger;

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
        return logger;
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
