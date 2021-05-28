package com.dfsek.terra.fabric;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
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
import com.dfsek.terra.api.event.events.config.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.transform.Validator;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.api.util.logging.Logger;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.fabric.config.PostLoadCompatibilityOptions;
import com.dfsek.terra.fabric.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.PopulatorFeature;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import com.dfsek.terra.fabric.handle.FabricItemHandle;
import com.dfsek.terra.fabric.handle.FabricWorldHandle;
import com.dfsek.terra.fabric.util.FabricUtil;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.exception.DuplicateEntryException;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class TerraFabricPlugin implements TerraPlugin, ModInitializer {
    private final org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger();
    public static final PopulatorFeature POPULATOR_FEATURE = new PopulatorFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredFeature<?, ?> POPULATOR_CONFIGURED_FEATURE = POPULATOR_FEATURE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(NopeDecoratorConfig.INSTANCE));
    private static TerraFabricPlugin instance;
    private final Map<DimensionType, Pair<ServerWorld, TerraWorld>> worldMap = new HashMap<>();

    public Map<DimensionType, Pair<ServerWorld, TerraWorld>> getWorldMap() {
        return worldMap;
    }

    private final EventManager eventManager = new TerraEventManager(this);
    private final GenericLoaders genericLoaders = new GenericLoaders(this);

    private final Profiler profiler = new ProfilerImpl();

    private final Logger logger = new Logger() {
        @Override
        public void info(String message) {
            log4jLogger.info(message);
        }

        @Override
        public void warning(String message) {
            log4jLogger.warn(message);
        }

        @Override
        public void severe(String message) {
            log4jLogger.error(message);
        }
    };

    private final DebugLogger debugLogger = new DebugLogger(logger);
    private final ItemHandle itemHandle = new FabricItemHandle();
    private final WorldHandle worldHandle = new FabricWorldHandle();
    private final ConfigRegistry registry = new ConfigRegistry();
    private final CheckedRegistry<ConfigPack> checkedRegistry = new CheckedRegistry<>(registry);

    private final FabricAddon fabricAddon = new FabricAddon(this);
    private final AddonRegistry addonRegistry = new AddonRegistry(fabricAddon, this);
    private final LockedRegistry<TerraAddon> addonLockedRegistry = new LockedRegistry<>(addonRegistry);

    private final PluginConfig config = new PluginConfig();

    private final Transformer<String, Biome> biomeFixer = new Transformer.Builder<String, Biome>()
            .addTransform(id -> BuiltinRegistries.BIOME.get(Identifier.tryParse(id)), Validator.notNull())
            .addTransform(id -> BuiltinRegistries.BIOME.get(Identifier.tryParse("minecraft:" + id.toLowerCase())), Validator.notNull()).build();
    private File dataFolder;
    private final CommandManager manager = new TerraCommandManager(this);

    public CommandManager getManager() {
        return manager;
    }

    public static TerraFabricPlugin getInstance() {
        return instance;
    }

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return getWorld(((WorldAccess) world).getDimension());
    }

    public TerraWorld getWorld(DimensionType type) {
        TerraWorld world = worldMap.get(type).getRight();
        if(world == null) throw new IllegalArgumentException("No world exists with dimension type " + type);
        return world;
    }

    @Override
    public Logger logger() {
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
        worldMap.forEach((seed, pair) -> {
            pair.getRight().getConfig().getSamplerCache().clear();
            String packID = pair.getRight().getConfig().getTemplate().getID();
            pair.setRight(new TerraWorld(pair.getRight().getWorld(), registry.get(packID), this));
        });
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
        return "Fabric";
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
                .registerLoader(com.dfsek.terra.api.platform.world.Biome.class, (t, o, l) -> biomeFixer.translate((String) o))
                .registerLoader(Identifier.class, (t, o, l) -> {
                    Identifier identifier = Identifier.tryParse((String) o);
                    if(identifier == null) throw new LoadException("Invalid identifier: " + o);
                    return identifier;
                });
    }

    public void packInit() {
        logger.info("Loading config packs...");
        registry.loadAll(this);

        registry.forEach(pack -> pack.getBiomeRegistry().forEach((id, biome) -> Registry.register(BuiltinRegistries.BIOME, new Identifier("terra", FabricUtil.createBiomeID(pack, id)), FabricUtil.createBiome(fabricAddon, biome, pack)))); // Register all Terra biomes.

        logger.info("Loaded packs.");
    }

    @Override
    public void onInitialize() {
        instance = this;

        this.dataFolder = new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra");
        saveDefaultConfig();
        config.load(this);
        debugLogger.setDebug(config.isDebug());
        LangUtil.load(config.getLanguage(), this);
        logger.info("Initializing Terra...");

        if(!addonRegistry.loadAll()) {
            throw new IllegalStateException("Failed to load addons. Please correct addon installations to continue.");
        }
        logger.info("Loaded addons.");


        Registry.register(Registry.FEATURE, new Identifier("terra", "populator"), POPULATOR_FEATURE);
        RegistryKey<ConfiguredFeature<?, ?>> floraKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("terra", "populator"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, floraKey.getValue(), POPULATOR_CONFIGURED_FEATURE);

        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("terra:terra"), FabricChunkGeneratorWrapper.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new Identifier("terra:terra"), TerraBiomeSource.CODEC);

        try {
            CommandUtil.registerAll(manager);
        } catch(MalformedCommandException e) {
            e.printStackTrace(); // TODO do something here even though this should literally never happen
        }

        logger.info("Finished initialization.");
    }


    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public Profiler getProfiler() {
        return profiler;
    }

    @Addon("Terra-Fabric")
    @Author("Terra")
    @Version("1.0.0")
    public static final class FabricAddon extends TerraAddon implements EventListener {

        private final TerraPlugin main;

        private final Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> templates = new HashMap<>();

        private FabricAddon(TerraPlugin main) {
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
            injectTree(treeRegistry, "BROWN_MUSHROOM", ConfiguredFeatures.HUGE_BROWN_MUSHROOM);
            injectTree(treeRegistry, "RED_MUSHROOM", ConfiguredFeatures.HUGE_RED_MUSHROOM);
            injectTree(treeRegistry, "JUNGLE", ConfiguredFeatures.MEGA_JUNGLE_TREE);
            injectTree(treeRegistry, "JUNGLE_COCOA", ConfiguredFeatures.JUNGLE_TREE);
            injectTree(treeRegistry, "LARGE_OAK", ConfiguredFeatures.FANCY_OAK);
            injectTree(treeRegistry, "LARGE_SPRUCE", ConfiguredFeatures.PINE);
            injectTree(treeRegistry, "SMALL_JUNGLE", ConfiguredFeatures.JUNGLE_TREE);
            injectTree(treeRegistry, "SWAMP_OAK", ConfiguredFeatures.SWAMP_OAK);
            injectTree(treeRegistry, "TALL_BIRCH", ConfiguredFeatures.BIRCH_TALL);
            injectTree(treeRegistry, "ACACIA", ConfiguredFeatures.ACACIA);
            injectTree(treeRegistry, "BIRCH", ConfiguredFeatures.BIRCH);
            injectTree(treeRegistry, "DARK_OAK", ConfiguredFeatures.DARK_OAK);
            injectTree(treeRegistry, "OAK", ConfiguredFeatures.OAK);
            injectTree(treeRegistry, "CHORUS_PLANT", ConfiguredFeatures.CHORUS_PLANT);
            injectTree(treeRegistry, "SPRUCE", ConfiguredFeatures.SPRUCE);
            injectTree(treeRegistry, "JUNGLE_BUSH", ConfiguredFeatures.JUNGLE_BUSH);
            injectTree(treeRegistry, "MEGA_SPRUCE", ConfiguredFeatures.MEGA_SPRUCE);
            injectTree(treeRegistry, "CRIMSON_FUNGUS", ConfiguredFeatures.CRIMSON_FUNGI);
            injectTree(treeRegistry, "WARPED_FUNGUS", ConfiguredFeatures.WARPED_FUNGI);

            PreLoadCompatibilityOptions template = new PreLoadCompatibilityOptions();
            try {
                event.loadTemplate(template);
            } catch(ConfigException e) {
                e.printStackTrace();
            }

            if(template.doRegistryInjection()) {
                BuiltinRegistries.CONFIGURED_FEATURE.getEntries().forEach(entry -> {
                    if(!template.getExcludedRegistryFeatures().contains(entry.getKey().getValue())) {
                        try {
                            event.getPack().getTreeRegistry().add(entry.getKey().getValue().toString(), (Tree) entry.getValue());
                            main.getDebugLogger().info("Injected ConfiguredFeature " + entry.getKey().getValue() + " as Tree.");
                        } catch(DuplicateEntryException ignored) {
                        }
                    }
                });
            }
            templates.put(event.getPack(), Pair.of(template, null));
        }

        @Priority(Priority.HIGHEST)
        @Global
        public void createInjectionOptions(ConfigPackPostLoadEvent event) {
            PostLoadCompatibilityOptions template = new PostLoadCompatibilityOptions();

            try {
                event.loadTemplate(template);
            } catch(ConfigException e) {
                e.printStackTrace();
            }

            templates.get(event.getPack()).setRight(template);
        }


        private void injectTree(CheckedRegistry<Tree> registry, String id, ConfiguredFeature<?, ?> tree) {
            try {
                registry.add(id, (Tree) tree);
            } catch(DuplicateEntryException ignore) {
            }
        }

        public Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> getTemplates() {
            return templates;
        }
    }
}
