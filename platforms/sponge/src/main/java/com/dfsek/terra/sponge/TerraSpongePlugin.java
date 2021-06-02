package com.dfsek.terra.sponge;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.addons.annotations.Addon;
import com.dfsek.terra.api.addons.annotations.Author;
import com.dfsek.terra.api.addons.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.TerraEventManager;
import com.dfsek.terra.api.event.annotations.Global;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.exception.DuplicateEntryException;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.sponge.handle.SpongeItemHandle;
import com.dfsek.terra.sponge.handle.SpongeWorldHandle;
import com.dfsek.terra.sponge.world.SpongeTree;
import com.dfsek.terra.world.TerraWorld;
import com.google.inject.Inject;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;


import java.io.File;
import java.io.IOException;

@Plugin("terra")
public class TerraSpongePlugin implements TerraPlugin {
    private final ConfigRegistry configRegistry = new ConfigRegistry();
    private final CheckedRegistry<ConfigPack> packCheckedRegistry = new CheckedRegistry<>(configRegistry);
    private final PluginConfig config = new PluginConfig();
    private final EventManager eventManager = new TerraEventManager(this);
    private final AddonRegistry addonRegistry;
    private final LockedRegistry<TerraAddon> addonLockedRegistry;



    private final PluginContainer plugin;

    private final GenericLoaders loaders = new GenericLoaders(this);

    private final WorldHandle worldHandle = new SpongeWorldHandle();

    Profiler profiler = new ProfilerImpl();

    @Inject
    public TerraSpongePlugin(PluginContainer plugin) {
        this.plugin = plugin;
        this.addonRegistry = new AddonRegistry(new SpongeAddon(this), this);
        this.addonLockedRegistry = new LockedRegistry<>(addonRegistry);
    }


    @Listener
    public void initialize(StartedEngineEvent<Server> event) {
        plugin.logger().info("Loading Terra...");
        plugin.logger().info("Config: " + getDataFolder());
        addonRegistry.loadAll();
        configRegistry.loadAll(this);
    }

    @Override
    public void register(TypeRegistry registry) {
        loaders.register(registry);
        registry.registerLoader(BlockData.class, (t, o, l) -> worldHandle.createBlockData((String) o));
    }

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return null;
    }

    @Override
    public com.dfsek.terra.api.util.logging.Logger logger() {
        return new SpongeLogger(plugin.logger());
    }

    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return Sponge.configManager().pluginConfig(plugin).directory().toFile();
    }

    @Override
    public boolean isDebug() {
        return true;
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
        return new SpongeItemHandle();
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
        return new DebugLogger(logger());
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public Profiler getProfiler() {
        return profiler;
    }

    @Addon("Terra-Sponge")
    @Author("Terra")
    @Version("1.0.0")
    private static final class SpongeAddon extends TerraAddon implements EventListener {
        private final TerraPlugin main;

        private SpongeAddon(TerraPlugin main) {
            this.main = main;
        }

        @Override
        public void initialize() {
            main.getEventManager().registerListener(this, this);
        }

        @Global
        public void onPackLoad(ConfigPackPreLoadEvent event) {
            CheckedRegistry<Tree> treeRegistry = event.getPack().getTreeRegistry();

            injectTree(treeRegistry, "BROWN_MUSHROOM", new SpongeTree());
            injectTree(treeRegistry, "RED_MUSHROOM", new SpongeTree());
            injectTree(treeRegistry, "JUNGLE", new SpongeTree());
            injectTree(treeRegistry, "JUNGLE_COCOA", new SpongeTree());
            injectTree(treeRegistry, "LARGE_OAK", new SpongeTree());
            injectTree(treeRegistry, "LARGE_SPRUCE", new SpongeTree());
            injectTree(treeRegistry, "SMALL_JUNGLE", new SpongeTree());
            injectTree(treeRegistry, "SWAMP_OAK", new SpongeTree());
            injectTree(treeRegistry, "TALL_BIRCH", new SpongeTree());
            injectTree(treeRegistry, "ACACIA", new SpongeTree());
            injectTree(treeRegistry, "BIRCH", new SpongeTree());
            injectTree(treeRegistry, "DARK_OAK", new SpongeTree());
            injectTree(treeRegistry, "OAK", new SpongeTree());
            injectTree(treeRegistry, "CHORUS_PLANT", new SpongeTree());
            injectTree(treeRegistry, "SPRUCE", new SpongeTree());
            injectTree(treeRegistry, "JUNGLE_BUSH", new SpongeTree());
            injectTree(treeRegistry, "MEGA_SPRUCE", new SpongeTree());
            injectTree(treeRegistry, "CRIMSON_FUNGUS", new SpongeTree());
            injectTree(treeRegistry, "WARPED_FUNGUS", new SpongeTree());
        }

        private void injectTree(CheckedRegistry<Tree> registry, String id, Tree tree) {
            try {
                registry.add(id, tree);
            } catch(DuplicateEntryException e) {
                e.printStackTrace();
            }
        }
    }
}
