package com.dfsek.terra.fabric;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.Logger;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.PluginConfig;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.lang.Language;
import com.dfsek.terra.api.profiler.Profiler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.commands.TerraCommandManager;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfigImpl;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.event.EventManagerImpl;
import com.dfsek.terra.fabric.config.PostLoadCompatibilityOptions;
import com.dfsek.terra.fabric.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.fabric.event.BiomeRegistrationEvent;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.PopulatorFeature;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import com.dfsek.terra.fabric.handle.FabricItemHandle;
import com.dfsek.terra.fabric.handle.FabricWorldHandle;
import com.dfsek.terra.fabric.util.FabricUtil;
import com.dfsek.terra.fabric.util.ProtoBiome;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.LockedRegistryImpl;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.util.logging.DebugLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
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
    public static final PopulatorFeature POPULATOR_FEATURE = new PopulatorFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredFeature<?, ?> POPULATOR_CONFIGURED_FEATURE = POPULATOR_FEATURE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(NopeDecoratorConfig.INSTANCE));
    private static TerraFabricPlugin instance;
    private final org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger();
    private final EventManager eventManager = new EventManagerImpl(this);
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
    private final ConfigRegistry configRegistry = new ConfigRegistry();
    private final CheckedRegistry<ConfigPack> checkedRegistry = new CheckedRegistryImpl<>(configRegistry);
    private final FabricAddon fabricAddon = new FabricAddon();
    private final AddonRegistry addonRegistry = new AddonRegistry(fabricAddon, this);
    private final com.dfsek.terra.api.registry.Registry<TerraAddon> addonLockedRegistry = new LockedRegistryImpl<>(addonRegistry);
    private final PluginConfig config = new PluginConfigImpl();
    private final CommandManager manager = new TerraCommandManager(this);
    private File dataFolder;

    public static TerraFabricPlugin getInstance() {
        return instance;
    }

    private ProtoBiome parseBiome(String id) throws LoadException {
        Identifier identifier = Identifier.tryParse(id);
        if(BuiltinRegistries.BIOME.get(identifier) == null) throw new LoadException("Invalid Biome ID: " + identifier); // failure.
        return new ProtoBiome(identifier);
    }

    public CommandManager getManager() {
        return manager;
    }

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
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
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }

    public FabricAddon getFabricAddon() {
        return fabricAddon;
    }

    @Override
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return checkedRegistry;
    }

    @Override
    public com.dfsek.terra.api.registry.Registry<TerraAddon> getAddons() {
        return addonLockedRegistry;
    }

    @Override
    public boolean reload() {
        config.load(this);
        LangUtil.load(config.getLanguage(), this); // Load language.
        boolean succeed = configRegistry.loadAll(this);
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
    public Logger getDebugLogger() {
        return debugLogger;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void register(TypeRegistry registry) {
        genericLoaders.register(registry);
        registry
                .registerLoader(BlockState.class, (t, o, l) -> worldHandle.createBlockData((String) o))
                .registerLoader(com.dfsek.terra.api.world.biome.Biome.class, (t, o, l) -> parseBiome((String) o))
                .registerLoader(Identifier.class, (t, o, l) -> {
                    Identifier identifier = Identifier.tryParse((String) o);
                    if(identifier == null) throw new LoadException("Invalid identifier: " + o);
                    return identifier;
                });
    }

    @Override
    public void onInitialize() {
        instance = this;

        this.dataFolder = new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra");
        saveDefaultConfig();
        config.load(this);
        LangUtil.load(config.getLanguage(), this);
        logger.info("Initializing Terra...");

        debugLogger.setDebug(config.isDebugLogging());
        if(config.isDebugProfiler()) profiler.start();

        if(!addonRegistry.loadAll(getClass().getClassLoader())) {
            throw new IllegalStateException("Failed to load addons. Please correct addon installations to continue.");
        }
        logger.info("Loaded addons.");


        Registry.register(Registry.FEATURE, new Identifier("terra", "populator"), POPULATOR_FEATURE);
        RegistryKey<ConfiguredFeature<?, ?>> floraKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier("terra", "populator"));
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
    public final class FabricAddon extends TerraAddon {
        private final Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> templates = new HashMap<>();

        @Override
        public void initialize() {
            eventManager
                    .getHandler(FunctionalEventHandler.class)
                    .register(this, ConfigPackPreLoadEvent.class)
                    .then(event -> {
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
                                        event.getPack().getCheckedRegistry(Tree.class).register(entry.getKey().getValue().toString(), (Tree) entry.getValue());
                                        debugLogger.info("Injected ConfiguredFeature " + entry.getKey().getValue() + " as Tree.");
                                    } catch(DuplicateEntryException ignored) {
                                    }
                                }
                            });
                        }
                        templates.put(event.getPack(), Pair.of(template, null));
                    })
                    .global();

            eventManager
                    .getHandler(FunctionalEventHandler.class)
                    .register(this, ConfigPackPostLoadEvent.class)
                    .then(event -> {
                        PostLoadCompatibilityOptions template = new PostLoadCompatibilityOptions();

                        try {
                            event.loadTemplate(template);
                        } catch(ConfigException e) {
                            e.printStackTrace();
                        }

                        templates.get(event.getPack()).setRight(template);
                    })
                    .priority(100)
                    .global();

            eventManager
                    .getHandler(FunctionalEventHandler.class)
                    .register(this, BiomeRegistrationEvent.class)
                    .then(event -> {
                        logger.info("Registering biomes...");
                        Registry<Biome> biomeRegistry = event.getRegistryManager().get(Registry.BIOME_KEY);
                        configRegistry.forEach(pack -> pack.getCheckedRegistry(TerraBiome.class).forEach((id, biome) -> FabricUtil.registerOrOverwrite(biomeRegistry, Registry.BIOME_KEY, new Identifier("terra", FabricUtil.createBiomeID(pack, id)), FabricUtil.createBiome(biome, pack, event.getRegistryManager())))); // Register all Terra biomes.
                        logger.info("Biomes registered.");
                    })
                    .global();
        }


        private void injectTree(CheckedRegistry<Tree> registry, String id, ConfiguredFeature<?, ?> tree) {
            try {
                registry.register(id, (Tree) tree);
            } catch(DuplicateEntryException ignore) {
            }
        }

        public Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> getTemplates() {
            return templates;
        }
    }
}
