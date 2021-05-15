package com.dfsek.terra.forge;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.addons.annotations.Addon;
import com.dfsek.terra.api.addons.annotations.Author;
import com.dfsek.terra.api.addons.annotations.Version;
import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.TerraCommandManager;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.TerraEventManager;
import com.dfsek.terra.api.event.annotations.Global;
import com.dfsek.terra.api.event.annotations.Priority;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.transform.NotNullValidator;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.util.JarUtil;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.forge.generation.ForgeChunkGeneratorWrapper;
import com.dfsek.terra.forge.generation.PopulatorFeature;
import com.dfsek.terra.forge.generation.TerraBiomeSource;
import com.dfsek.terra.forge.handle.ForgeItemHandle;
import com.dfsek.terra.forge.handle.ForgeWorldHandle;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.exception.DuplicateEntryException;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.DecoratedPlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

@Mod("terra")
@Mod.EventBusSubscriber(modid = "terra", bus = Mod.EventBusSubscriber.Bus.MOD)
public class TerraForgePlugin implements TerraPlugin {
    public static final PopulatorFeature POPULATOR_FEATURE = (PopulatorFeature) new PopulatorFeature(NoFeatureConfig.CODEC).setRegistryName("terra", "terra");
    public static final ConfiguredFeature<?, ?> POPULATOR_CONFIGURED_FEATURE = POPULATOR_FEATURE.configured(IFeatureConfig.NONE).decorated(DecoratedPlacement.NOPE.configured(NoPlacementConfig.INSTANCE));

    private static TerraForgePlugin INSTANCE;
    private final Map<Long, TerraWorld> worldMap = new HashMap<>();
    private final EventManager eventManager = new TerraEventManager(this);
    private final GenericLoaders genericLoaders = new GenericLoaders(this);
    private final Profiler profiler = new ProfilerImpl();

    private final CommandManager manager = new TerraCommandManager(this);

    private final com.dfsek.terra.api.util.logging.Logger logger = new com.dfsek.terra.api.util.logging.Logger() {
        private final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

        @Override
        public void info(String message) {
            logger.info(message);
        }

        @Override
        public void warning(String message) {
            logger.warn(message);
        }

        @Override
        public void severe(String message) {
            logger.error(message);
        }
    };

    private final DebugLogger debugLogger = new DebugLogger(logger);
    private final ItemHandle itemHandle = new ForgeItemHandle();
    private final WorldHandle worldHandle = new ForgeWorldHandle();
    private final ConfigRegistry registry = new ConfigRegistry();
    private final CheckedRegistry<ConfigPack> checkedRegistry = new CheckedRegistry<>(registry);
    private final AddonRegistry addonRegistry = new AddonRegistry(new ForgeAddon(this), this);
    private final LockedRegistry<TerraAddon> addonLockedRegistry = new LockedRegistry<>(addonRegistry);
    private final PluginConfig config = new PluginConfig();
    private final Transformer<String, Biome> biomeFixer = new Transformer.Builder<String, Biome>()
            .addTransform(id -> ForgeRegistries.BIOMES.getValue(ResourceLocation.tryParse(id)), new NotNullValidator<>())
            .addTransform(id -> ForgeRegistries.BIOMES.getValue(ResourceLocation.tryParse("minecraft:" + id.toLowerCase())), new NotNullValidator<>()).build();
    private final File dataFolder;

    public TerraForgePlugin() {
        if(INSTANCE != null) throw new IllegalStateException("Only one TerraPlugin instance may exist.");
        INSTANCE = this;
        this.dataFolder = Paths.get("config", "Terra").toFile();
        saveDefaultConfig();
        config.load(this);
        LangUtil.load(config.getLanguage(), this);
        try {
            CommandUtil.registerAll(manager);
        } catch(MalformedCommandException e) {
            e.printStackTrace(); // TODO do something here even though this should literally never happen
        }
    }

    public static TerraForgePlugin getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public static void setupListener(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Registry.register(Registry.BIOME_SOURCE, "terra:biome", TerraBiomeSource.CODEC);
            Registry.register(Registry.CHUNK_GENERATOR, "terra:generator", ForgeChunkGeneratorWrapper.CODEC);
        });
    }

    public void setup() {
        logger.info("Initializing Terra...");

        if(!addonRegistry.loadAll()) {
            throw new IllegalStateException("Failed to load addons. Please correct addon installations to continue.");
        }
        logger.info("Loaded addons.");

        registry.loadAll(this);
        logger.info("Loaded packs.");
    }

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return worldMap.computeIfAbsent(world.getSeed(), w -> {
            logger.info("Loading world " + w);
            return new TerraWorld(world, ((ForgeChunkGeneratorWrapper) world.getGenerator()).getPack(), this);
        });
    }

    /**
     * evil code brought to you by Forge Mod Loader
     * <p>
     * Forge changes the JAR URI to something that cannot
     * be resolved back to the original JAR, so we have to
     * do this to get our JAR.
     */
    @Override
    public JarFile getModJar() throws URISyntaxException, IOException {
        File modsDir = new File("./mods");

        if(!modsDir.exists()) return JarUtil.getJarFile();

        for(File file : Objects.requireNonNull(modsDir.listFiles((dir, name) -> name.endsWith(".jar")))) {
            try(ZipFile zipFile = new ZipFile(file)) {
                if(zipFile.getEntry(Type.getInternalName(TerraPlugin.class) + ".class") != null) {
                    return new JarFile(file);
                }
            }
        }
        return JarUtil.getJarFile();
    }

    public TerraWorld getWorld(long seed) {
        TerraWorld world = worldMap.get(seed);
        if(world == null) throw new IllegalArgumentException("No world exists with seed " + seed);
        return world;
    }

    @Override
    public com.dfsek.terra.api.util.logging.Logger logger() {
        return logger;
    }

    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public boolean isDebug() {
        return config.isDebug();
    }

    @Override
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }

    @Override
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return checkedRegistry;
    }

    @Override
    public LockedRegistry<TerraAddon> getAddons() {
        return addonLockedRegistry;
    }

    @Override
    public boolean reload() {
        config.load(this);
        LangUtil.load(config.getLanguage(), this); // Load language.
        boolean succeed = registry.loadAll(this);
        Map<Long, TerraWorld> newMap = new HashMap<>();
        worldMap.forEach((seed, tw) -> {
            tw.getConfig().getSamplerCache().clear();
            String packID = tw.getConfig().getTemplate().getID();
            newMap.put(seed, new TerraWorld(tw.getWorld(), registry.get(packID), this));
        });
        worldMap.clear();
        worldMap.putAll(newMap);
        return succeed;
    }

    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }

    @Override
    public void saveDefaultConfig() {
        try(InputStream stream = getClass().getResourceAsStream("/config.yml")) {
            File configFile = new File(getDataFolder(), "config.yml");
            if(!configFile.exists()) FileUtils.copyInputStreamToFile(stream, configFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String platformName() {
        return "Forge";
    }

    @Override
    public DebugLogger getDebugLogger() {
        return debugLogger;
    }

    @Override
    public void register(TypeRegistry registry) {
        genericLoaders.register(registry);
        registry
                .registerLoader(BlockData.class, (t, o, l) -> worldHandle.createBlockData((String) o))
                .registerLoader(com.dfsek.terra.api.platform.world.Biome.class, (t, o, l) -> biomeFixer.translate((String) o));
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public Profiler getProfiler() {
        return profiler;
    }

    public CommandManager getManager() {
        return manager;
    }

    @Addon("Terra-Forge")
    @Author("Terra")
    @Version("1.0.0")
    private static final class ForgeAddon extends TerraAddon implements EventListener {

        private final TerraPlugin main;

        private ForgeAddon(TerraPlugin main) {
            this.main = main;
        }

        @Override
        public void initialize() {
            main.getEventManager().registerListener(this, this);
        }

        @Priority(Priority.LOWEST)
        @Global
        public void injectTrees(ConfigPackPreLoadEvent event) {
            CheckedRegistry<Tree> treeRegistry = event.getPack().getTreeRegistry();
            injectTree(treeRegistry, "BROWN_MUSHROOM", Features.HUGE_BROWN_MUSHROOM);
            injectTree(treeRegistry, "RED_MUSHROOM", Features.HUGE_RED_MUSHROOM);
            injectTree(treeRegistry, "JUNGLE", Features.MEGA_JUNGLE_TREE);
            injectTree(treeRegistry, "JUNGLE_COCOA", Features.JUNGLE_TREE);
            injectTree(treeRegistry, "LARGE_OAK", Features.FANCY_OAK);
            injectTree(treeRegistry, "LARGE_SPRUCE", Features.PINE);
            injectTree(treeRegistry, "SMALL_JUNGLE", Features.JUNGLE_TREE);
            injectTree(treeRegistry, "SWAMP_OAK", Features.SWAMP_TREE);
            injectTree(treeRegistry, "TALL_BIRCH", Features.BIRCH_TALL);
            injectTree(treeRegistry, "ACACIA", Features.ACACIA);
            injectTree(treeRegistry, "BIRCH", Features.BIRCH);
            injectTree(treeRegistry, "DARK_OAK", Features.DARK_OAK);
            injectTree(treeRegistry, "OAK", Features.OAK);
            injectTree(treeRegistry, "CHORUS_PLANT", Features.CHORUS_PLANT);
            injectTree(treeRegistry, "SPRUCE", Features.SPRUCE);
            injectTree(treeRegistry, "JUNGLE_BUSH", Features.JUNGLE_BUSH);
            injectTree(treeRegistry, "MEGA_SPRUCE", Features.MEGA_SPRUCE);
            injectTree(treeRegistry, "CRIMSON_FUNGUS", Features.CRIMSON_FUNGI);
            injectTree(treeRegistry, "WARPED_FUNGUS", Features.WARPED_FUNGI);
        }


        private void injectTree(CheckedRegistry<Tree> registry, String id, ConfiguredFeature<?, ?> tree) {
            try {
                registry.add(id, (Tree) tree);
            } catch(DuplicateEntryException ignore) {
            }
        }
    }
}
