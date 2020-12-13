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
import com.dfsek.terra.sponge.world.SpongeWorldHandle;
import com.google.inject.Inject;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

@Plugin(id = "terra", name = "Terra", version = "@VERSION", description = "Terra world generator")
public class TerraSpongePlugin implements TerraPlugin {

    private final ConfigRegistry registry = new ConfigRegistry();

    private final WorldHandle worldHandle = new SpongeWorldHandle(this);

    @Inject
    private Logger logger;
    private boolean enabled = false;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    @Inject
    private GameRegistry gameRegistry;

    public GameRegistry getGameRegistry() {
        return gameRegistry;
    }

    @Listener
    public void serverStart(GameStartedServerEvent event) {
        logger.info("Hello Sponge!");
        logger.info("Config dir is: " + configDir);
        registry.loadAll(this);
        enabled = true;
    }

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
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
        return configDir;
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
