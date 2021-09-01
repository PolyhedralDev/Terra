package com.dfsek.terra.forge;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeRegistry;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
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
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.transform.Validator;
import com.dfsek.terra.api.util.JarUtil;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.forge.config.PostLoadCompatibilityOptions;
import com.dfsek.terra.forge.config.PreLoadCompatibilityOptions;
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
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.DecoratedPlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
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
    public static final PopulatorFeature POPULATOR_FEATURE = (PopulatorFeature) new PopulatorFeature(NoFeatureConfig.CODEC).setRegistryName(
            "terra", "terra");
    public static final ConfiguredFeature<?, ?> POPULATOR_CONFIGURED_FEATURE = POPULATOR_FEATURE.configured(IFeatureConfig.NONE).decorated(
            DecoratedPlacement.NOPE.configured(NoPlacementConfig.INSTANCE));
    
    private static TerraForgePlugin INSTANCE;
    private final Map<DimensionType, Pair<ServerWorld, TerraWorld>> worldMap = new HashMap<>();
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
    
    private final ForgeAddon addon = new ForgeAddon(this);
    
    private final AddonRegistry addonRegistry = new AddonRegistry(addon, this);
    private final LockedRegistry<TerraAddon> addonLockedRegistry = new LockedRegistry<>(addonRegistry);
    private final PluginConfig config = new PluginConfig();
    private final Transformer<String, Biome> biomeFixer = new Transformer.Builder<String, Biome>()
            .addTransform(id -> ForgeRegistries.BIOMES.getValue(ResourceLocation.tryParse(id)), Validator.notNull())
            .addTransform(id -> ForgeRegistries.BIOMES.getValue(ResourceLocation.tryParse("minecraft:" + id.toLowerCase())),
                          Validator.notNull()).build();
    private final File dataFolder;
    
    public TerraForgePlugin() {
        if(INSTANCE != null) throw new IllegalStateException("Only one TerraPlugin instance may exist.");
        INSTANCE = this;
        this.dataFolder = Paths.get("config", "Terra").toFile();
        saveDefaultConfig();
        config.load(this);
        debugLogger.setDebug(config.isDebug());
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
    
    public void init() {
        logger.info("Initializing Terra...");
        
        if(!addonRegistry.loadAll()) {
            throw new IllegalStateException("Failed to load addons. Please correct com.dfsek.terra.addon installations to continue.");
        }
        logger.info("Loaded addons.");
        
        registry.loadAll(this);
        logger.info("Loaded packs.");
        
        ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).unfreeze(); // Evil
        getConfigRegistry().forEach(pack -> pack.getBiomeRegistry()
                                                .forEach((id, biome) -> ForgeRegistries.BIOMES.register(
                                                        ForgeUtil.createBiome(biome, pack, addon)))); // Register all Terra biomes.
        ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).freeze();
    }
    
    @Override
    public com.dfsek.terra.api.util.logging.Logger logger() {
        return logger;
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
    public void register(TypeRegistry registry) {
        genericLoaders.register(registry);
        registry
                .registerLoader(BlockData.class, (t, o, l) -> worldHandle.createBlockData((String) o))
                .registerLoader(com.dfsek.terra.api.world.Biome.class, (t, o, l) -> biomeFixer.translate((String) o))
                .registerLoader(ResourceLocation.class, (t, o, l) -> {
                    ResourceLocation identifier = ResourceLocation.tryParse((String) o);
                    if(identifier == null) throw new LoadException("Invalid identifier: " + o);
                    return identifier;
                });
    }
    
    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }
    
    @Override
    public TerraWorld getWorld(World world) {
        return getWorld(((IWorld) world).dimensionType());
    }
    
    public TerraWorld getWorld(DimensionType type) {
        TerraWorld world = worldMap.get(type).getRight();
        if(world == null) throw new IllegalArgumentException("No world exists with dimension type " + type);
        return world;
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
    public ItemHandle getItemHandle() {
        return itemHandle;
    }
    
    @Override
    public DebugLogger getDebugLogger() {
        return debugLogger;
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
    
    public Map<DimensionType, Pair<ServerWorld, TerraWorld>> getWorldMap() {
        return worldMap;
    }
    
    @Addon("Terra-Forge")
    @Author("Terra")
    @Version("1.0.0")
    public static final class ForgeAddon extends TerraAddon implements EventListener {
        
        private final Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> templates = new HashMap<>();
        
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
            CheckedRegistry<Tree> treeRegistry = event.getPack().getRegistry(Tree.class);
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
            PreLoadCompatibilityOptions template = new PreLoadCompatibilityOptions();
            try {
                event.loadTemplate(template);
            } catch(ConfigException e) {
                e.printStackTrace();
            }
            
            if(template.doRegistryInjection()) {
                WorldGenRegistries.CONFIGURED_FEATURE.entrySet().forEach(entry -> {
                    if(!template.getExcludedRegistryFeatures().contains(entry.getKey().getRegistryName())) {
                        try {
                            event.getPack().getTreeRegistry().add(entry.getKey().getRegistryName().toString(), (Tree) entry.getValue());
                            main.getDebugLogger().info(
                                    "Injected ConfiguredFeature " + entry.getKey().getRegistryName() + " as Tree: " + entry.getValue());
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
