package com.dfsek.terra.minestom;

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
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.api.util.logging.Logger;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.minestom.world.MinestomBiome;
import com.dfsek.terra.minestom.world.MinestomChunkAccess;
import com.dfsek.terra.minestom.world.MinestomTree;
import com.dfsek.terra.minestom.world.MinestomWorld;
import com.dfsek.terra.minestom.world.MinestomWorldHandle;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minestom.server.instance.Instance;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.Map;

public class TerraMinestomPlugin implements TerraPlugin {
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger();
    private final MinestomWorldHandle worldHandle = new MinestomWorldHandle();
    private final PluginConfig config = new PluginConfig();
    private final EventManager eventManager = new TerraEventManager(this);
    private final ConfigRegistry configRegistry = new ConfigRegistry();
    private final AddonRegistry addonRegistry = new AddonRegistry(new MinestomPlugin(this), this);
    private final GenericLoaders genericLoaders = new GenericLoaders(this);
    private final MinestomItemHandle itemHandle = new MinestomItemHandle();
    private final Map<Instance, TerraWorld> instanceTerraWorldMap = new Object2ObjectOpenHashMap<>();

    public TerraMinestomPlugin() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        LangUtil.load(config.getLanguage(), this);
        configRegistry.loadAll(this);
        addonRegistry.loadAll();
    }

    @Override
    public void register(TypeRegistry registry) {
        registry
                .registerLoader(BlockData.class, (t, o, l) -> worldHandle.createBlockData((String) o))
                .registerLoader(Biome.class, (t, o, l) -> new MinestomBiome(net.minestom.server.world.biomes.Biome.PLAINS));
        genericLoaders.register(registry);
    }

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public TerraWorld getWorld(World world) {
        if(world instanceof MinestomWorld) {
            return instanceTerraWorldMap.computeIfAbsent(((MinestomWorld) world).getHandle(), instance -> new TerraWorld(new MinestomWorld(instance), configRegistry.get("DEFAULT"), this));
        } else {
            return instanceTerraWorldMap.computeIfAbsent(((MinestomWorld) ((MinestomChunkAccess) world).getWorld()).getHandle(), instance -> new TerraWorld(new MinestomWorld(instance), configRegistry.get("DEFAULT"), this));
        }
    }

    @Override
    public Logger logger() {
        return new Logger() {
            @Override
            public void info(String message) {
                System.out.println(message);
            }

            @Override
            public void warning(String message) {
                System.out.println(message);
            }

            @Override
            public void severe(String message) {
                System.err.println(message);
            }
        };
    }

    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return new File("Terra");
    }

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }

    @Override
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return new CheckedRegistry<>(configRegistry);
    }

    @Override
    public LockedRegistry<TerraAddon> getAddons() {
        return new LockedRegistry<>(addonRegistry);
    }

    @Override
    public boolean reload() {
        return true;
    }

    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }

    @Override
    public void saveDefaultConfig() {

    }

    @Override
    public String platformName() {
        return "Minestom";
    }

    @Override
    public DebugLogger getDebugLogger() {
        return new DebugLogger(logger());
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Addon("Terra-Minestom")
    @Author("Terra")
    @Version("1.0.0")
    private static final class MinestomPlugin extends TerraAddon implements EventListener {
        private final TerraPlugin main;

        private MinestomPlugin(TerraPlugin main) {
            this.main = main;
        }

        @Override
        public void initialize() {
            main.getEventManager().registerListener(this, this);
        }

        @Global
        public void registerTrees(ConfigPackPreLoadEvent event) {
            CheckedRegistry<Tree> treeCheckedRegistry = event.getPack().getTreeRegistry();

            treeCheckedRegistry.addUnchecked("BIRCH", new MinestomTree());
            treeCheckedRegistry.addUnchecked("JUNGLE_BUSH", new MinestomTree());
            treeCheckedRegistry.addUnchecked("SWAMP_OAK", new MinestomTree());
            treeCheckedRegistry.addUnchecked("JUNGLE_COCOA", new MinestomTree());
            treeCheckedRegistry.addUnchecked("RED_MUSHROOM", new MinestomTree());
            treeCheckedRegistry.addUnchecked("BROWN_MUSHROOM", new MinestomTree());
        }
    }
}
