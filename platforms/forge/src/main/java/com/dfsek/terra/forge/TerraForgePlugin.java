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
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.forge.inventory.ForgeItemHandle;
import com.dfsek.terra.forge.world.ForgeBiome;
import com.dfsek.terra.forge.world.ForgeTree;
import com.dfsek.terra.forge.world.ForgeWorldHandle;
import com.dfsek.terra.forge.world.TerraBiomeSource;
import com.dfsek.terra.forge.world.features.PopulatorFeature;
import com.dfsek.terra.forge.world.generator.ForgeChunkGenerator;
import com.dfsek.terra.forge.world.generator.ForgeChunkGeneratorWrapper;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.exception.DuplicateEntryException;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.DecoratedPlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeWorldTypeScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Mod("terra")
@Mod.EventBusSubscriber(modid = "terra", bus = Mod.EventBusSubscriber.Bus.MOD)
public class TerraForgePlugin implements TerraPlugin {
    public static final PopulatorFeature POPULATOR_FEATURE = new PopulatorFeature(NoFeatureConfig.CODEC);
    public static final ConfiguredFeature<?, ?> POPULATOR_CONFIGURED_FEATURE = POPULATOR_FEATURE.configured(IFeatureConfig.NONE).decorated(DecoratedPlacement.NOPE.configured(NoPlacementConfig.INSTANCE));

    private static TerraForgePlugin INSTANCE;
    private final Map<Long, TerraWorld> worldMap = new HashMap<>();
    private final EventManager eventManager = new TerraEventManager(this);
    private final GenericLoaders genericLoaders = new GenericLoaders(this);
    private final Profiler profiler = new ProfilerImpl();
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
    private File dataFolder;

    public TerraForgePlugin() {
        if(INSTANCE != null) throw new IllegalStateException("Only one TerraPlugin instance may exist.");
        INSTANCE = this;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static TerraForgePlugin getInstance() {
        return INSTANCE;
    }

    public static String createBiomeID(ConfigPack pack, String biomeID) {
        return pack.getTemplate().getID().toLowerCase() + "/" + biomeID.toLowerCase(Locale.ROOT);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Biome> event) {
        INSTANCE.registry.forEach(pack -> pack.getBiomeRegistry().forEach((id, biome) -> event.getRegistry().register(createBiome(biome)))); // Register all Terra biomes.
    }

    @SubscribeEvent
    public static void registerLevels(RegistryEvent.Register<ForgeWorldType> event) {
        getInstance().logger.info("Registering level types...");
        event.getRegistry().register(TerraLevelType.FORGE_WORLD_TYPE);
    }

    private static Biome createBiome(BiomeBuilder biome) {
        BiomeTemplate template = biome.getTemplate();
        Map<String, Integer> colors = template.getColors();

        Biome vanilla = ((ForgeBiome) new ArrayList<>(biome.getVanillaBiomes().getContents()).get(0)).getHandle();

        BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder();
        generationSettings.surfaceBuilder(SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.GRAVEL.defaultBlockState()))); // It needs a surfacebuilder, even though we dont use it.
        generationSettings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, POPULATOR_CONFIGURED_FEATURE);


        /*
        BiomeAmbience.Builder effects = new BiomeAmbience.Builder()
                .waterColor(colors.getOrDefault("water", vanilla.getSpecialEffects().waterColor))
                .waterFogColor(colors.getOrDefault("water-fog", vanilla.getSpecialEffects().waterFogColor))
                .fogColor(colors.getOrDefault("fog", vanilla.getSpecialEffects().fogColor))
                .skyColor(colors.getOrDefault("sky", vanilla.getSpecialEffects().skyColor))
                .grassColorModifier(vanilla.getSpecialEffects().grassColorModifier);

        if(colors.containsKey("grass")) {
            effects.grassColorOverride(colors.get("grass"));
        } else {
            vanilla.getSpecialEffects().grassColor.ifPresent(effects::grassColor);
        }
        vanilla.getEffects().foliageColor.ifPresent(effects::foliageColor);
        if(colors.containsKey("foliage")) {
            effects.foliageColor(colors.get("foliage"));
        } else {
            vanilla.getEffects().foliageColor.ifPresent(effects::foliageColor);
        }

         */

        return new Biome.Builder()
                .precipitation(vanilla.getPrecipitation())
                .biomeCategory(vanilla.getBiomeCategory())
                .depth(vanilla.getDepth())
                .scale(vanilla.getScale())
                .temperature(vanilla.getBaseTemperature())
                .downfall(vanilla.getDownfall())
                //.specialEffects(effects.build())
                .specialEffects(vanilla.getSpecialEffects())
                .mobSpawnSettings(vanilla.getMobSettings())
                .generationSettings(generationSettings.build())
                .build();
    }

    public void setup(FMLCommonSetupEvent event) {
        this.dataFolder = Paths.get("config", "Terra").toFile();
        saveDefaultConfig();
        config.load(this);
        LangUtil.load(config.getLanguage(), this);
        logger.info("Initializing Terra...");

        if(!addonRegistry.loadAll()) {
            throw new IllegalStateException("Failed to load addons. Please correct addon installations to continue.");
        }
        logger.info("Loaded addons.");

        registry.loadAll(this);

        logger.info("Loaded packs.");

        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation("terra:terra"), ForgeChunkGeneratorWrapper.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation("terra:terra"), TerraBiomeSource.CODEC);

        CommandManager manager = new TerraCommandManager(this);
        try {
            CommandUtil.registerAll(manager);
        } catch(MalformedCommandException e) {
            e.printStackTrace(); // TODO do something here even though this should literally never happen
        }


        /*
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                    int max = manager.getMaxArgumentDepth();
                    RequiredArgumentBuilder<ServerCommandSource, String> arg = argument("arg" + (max - 1), StringArgumentType.word());
                    for(int i = 0; i < max; i++) {
                        RequiredArgumentBuilder<ServerCommandSource, String> next = argument("arg" + (max - i - 1), StringArgumentType.word());

                        arg = next.then(assemble(arg, manager));
                    }

                    dispatcher.register(literal("terra").executes(context -> 1).then(assemble(arg, manager)));
                    dispatcher.register(literal("te").executes(context -> 1).then(assemble(arg, manager)));
                    //dispatcher.register(literal("te").redirect(root));
                }
        );

         */
    }

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return worldMap.computeIfAbsent(world.getSeed(), w -> {
            logger.info("Loading world " + w);
            return new TerraWorld(world, ((ForgeChunkGeneratorWrapper) ((ForgeChunkGenerator) world.getGenerator()).getHandle()).getPack(), this);
        });
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
        return true;
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
                .registerLoader(com.dfsek.terra.api.platform.world.Biome.class, (t, o, l) -> new ForgeBiome(biomeFixer.translate((String) o)));
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    /*
    private RequiredArgumentBuilder<ServerCommandSource, String> assemble(RequiredArgumentBuilder<ServerCommandSource, String> in, CommandManager manager) {
        return in.suggests((context, builder) -> {
            List<String> args = parseCommand(context.getInput());
            CommandSender sender = FabricAdapter.adapt(context.getSource());
            try {
                manager.tabComplete(args.remove(0), sender, args).forEach(builder::suggest);
            } catch(CommandException e) {
                sender.sendMessage(e.getMessage());
            }
            return builder.buildFuture();
        }).executes(context -> {
            List<String> args = parseCommand(context.getInput());
            try {
                manager.execute(args.remove(0), FabricAdapter.adapt(context.getSource()), args);
            } catch(CommandException e) {
                context.getSource().sendError(new LiteralText(e.getMessage()));
            }
            return 1;
        });
    }

    private List<String> parseCommand(String command) {
        if(command.startsWith("/terra ")) command = command.substring("/terra ".length());
        else if(command.startsWith("/te ")) command = command.substring("/te ".length());
        List<String> c = new ArrayList<>(Arrays.asList(command.split(" ")));
        if(command.endsWith(" ")) c.add("");
        return c;
    }

     */

    @Override
    public Profiler getProfiler() {
        return profiler;
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class ClientEvents {
        @SubscribeEvent
        public static void register(FMLClientSetupEvent event) {
            getInstance().logger.info("Client setup...");

            ForgeWorldType world = TerraLevelType.FORGE_WORLD_TYPE;
            ForgeWorldTypeScreens.registerFactory(world, (returnTo, dimensionGeneratorSettings) -> new Screen(world.getDisplayName()) {
                @Override
                protected void init() {
                    addButton(new Button(0, 0, 120, 20, new StringTextComponent("close"), btn -> Minecraft.getInstance().setScreen(returnTo)));
                }
            });
        }
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
                registry.add(id, new ForgeTree(tree, id, TerraForgePlugin.getInstance()));
            } catch(DuplicateEntryException ignore) {
            }
        }
    }
}
