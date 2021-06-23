package com.dfsek.terra.sponge;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.EventManagerImpl;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.sponge.world.SpongeWorldHandle;
import com.dfsek.terra.world.TerraWorld;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = "terra", name = "Terra-Sponge", version = "", description = "Terra")
public class TerraSpongePlugin implements TerraPlugin {
    private final ConfigRegistry configRegistry = new ConfigRegistry();
    private final CheckedRegistry<ConfigPack> packCheckedRegistry = new CheckedRegistry<>(configRegistry);
    private final PluginConfig config = new PluginConfig();
    private final AddonRegistry addonRegistry = new AddonRegistry(this);
    private final LockedRegistry<TerraAddon> addonLockedRegistry = new LockedRegistry<>(addonRegistry);

    private final SpongeWorldHandle spongeWorldHandle = new SpongeWorldHandle();

    private final EventManager eventManager = new EventManagerImpl(this);

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;

    @Inject
    private Logger logger;


    @Listener
    public void initialize(GameStartedServerEvent event) {
        addonRegistry.loadAll();
        configRegistry.loadAll(this);
    }

    @Override
    public void register(TypeRegistry registry) {

    }

    @Override
    public WorldHandle getWorldHandle() {
        return spongeWorldHandle;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return null;
    }

    @Override
    public com.dfsek.terra.api.util.logging.Logger logger() {
        return new SpongeLogger(logger);
    }

    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return privateConfigDir.toFile();
    }

    @Override
    public Language getLanguage() {
        try {
            return new Language(new File(getDataFolder(), "lang/en_us.yml"));
        } catch(IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return packCheckedRegistry;
    }

    @Override
    public LockedRegistry<TerraAddon> getAddons() {
        return addonLockedRegistry;
    }

    @Override
    public boolean reload() {
        return false;
    }

    @Override
    public ItemHandle getItemHandle() {
        return null;
    }

    @Override
    public void saveDefaultConfig() {

    }

    @Override
    public String platformName() {
        return "Sponge";
    }

    @Override
    public DebugLogger getDebugLogger() {
        return null;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public Profiler getProfiler() {
        return null;
    }
}
