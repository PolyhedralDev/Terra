package com.dfsek.terra.fabric;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.core.event.EventManager;
import com.dfsek.terra.api.core.event.TerraEventManager;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.transform.MapTransform;
import com.dfsek.terra.api.transform.NotNullValidator;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.debug.DebugLogger;
import com.dfsek.terra.fabric.inventory.FabricItemHandle;
import com.dfsek.terra.fabric.mixin.GeneratorTypeAccessor;
import com.dfsek.terra.fabric.world.FabricBiome;
import com.dfsek.terra.fabric.world.FabricWorldHandle;
import com.dfsek.terra.fabric.world.TerraBiomeSource;
import com.dfsek.terra.fabric.world.features.PopulatorFeature;
import com.dfsek.terra.fabric.world.generator.FabricChunkGeneratorWrapper;
import com.dfsek.terra.registry.AddonRegistry;
import com.dfsek.terra.registry.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TerraFabricPlugin implements TerraPlugin, ModInitializer {

    private final Map<Long, TerraWorld> worldMap = new HashMap<>();
    private static TerraFabricPlugin instance;

    private final EventManager eventManager = new TerraEventManager(this);

    public static TerraFabricPlugin getInstance() {
        return instance;
    }

    public static final PopulatorFeature POPULATOR_FEATURE = new PopulatorFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredFeature<?, ?> POPULATOR_CONFIGURED_FEATURE = POPULATOR_FEATURE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(NopeDecoratorConfig.INSTANCE));

    private final GenericLoaders genericLoaders = new GenericLoaders(this);
    private final Logger logger = Logger.getLogger("Terra");
    private final DebugLogger debugLogger = new DebugLogger(logger);
    private final ItemHandle itemHandle = new FabricItemHandle();
    private final WorldHandle worldHandle = new FabricWorldHandle();
    private final ConfigRegistry registry = new ConfigRegistry();

    private final AddonRegistry addonRegistry = new AddonRegistry();
    private File config;
    private static final Transformer<String, ConfiguredFeature<?, ?>> TREE_TRANSFORMER = new Transformer.Builder<String, ConfiguredFeature<?, ?>>()
            .addTransform(TerraFabricPlugin::getFeature)
            .addTransform(new MapTransform<String, ConfiguredFeature<?, ?>>()
                    .add("BROWN_MUSHROOM", ConfiguredFeatures.BROWN_MUSHROOM_GIANT)
                    .add("RED_MUSHROOM", ConfiguredFeatures.RED_MUSHROOM_GIANT)
                    .add("JUNGLE", ConfiguredFeatures.MEGA_JUNGLE_TREE)
                    .add("JUNGLE_COCOA", ConfiguredFeatures.JUNGLE_TREE)
                    .add("LARGE_OAK", ConfiguredFeatures.FANCY_OAK)
                    .add("LARGE_SPRUCE", ConfiguredFeatures.PINE)
                    .add("SMALL_JUNGLE", ConfiguredFeatures.JUNGLE_TREE)
                    .add("SWAMP_OAK", ConfiguredFeatures.SWAMP_TREE)
                    .add("TALL_BIRCH", ConfiguredFeatures.BIRCH_TALL)
                    .add("ACACIA", ConfiguredFeatures.ACACIA)
                    .add("BIRCH", ConfiguredFeatures.BIRCH)
                    .add("DARK_OAK", ConfiguredFeatures.DARK_OAK)
                    .add("OAK", ConfiguredFeatures.OAK)
                    .add("CHORUS_PLANT", ConfiguredFeatures.CHORUS_PLANT)
                    .add("SPRUCE", ConfiguredFeatures.SPRUCE)
                    .add("JUNGLE_BUSH", ConfiguredFeatures.JUNGLE_BUSH)
                    .add("MEGA_SPRUCE", ConfiguredFeatures.MEGA_SPRUCE)
                    .add("CRIMSON_FUNGUS", ConfiguredFeatures.CRIMSON_FUNGI)
                    .add("WARPED_FUNGUS", ConfiguredFeatures.WARPED_FUNGI)).build();
    private final PluginConfig plugin = new PluginConfig();

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return worldMap.computeIfAbsent(world.getSeed(), w -> {
            logger.info("Loading world " + w);
            return new TerraWorld(world, getRegistry().get("DEFAULT"), this);
        });
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public PluginConfig getTerraConfig() {
        return plugin;
    }

    @Override
    public File getDataFolder() {
        return config;
    }

    @Override
    public boolean isDebug() {
        return false;
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
    public ConfigRegistry getRegistry() {
        return registry;
    }

    @Override
    public AddonRegistry getAddons() {
        return addonRegistry;
    }

    @Override
    public void reload() {

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

    Transformer<String, Biome> biomeFixer = new Transformer.Builder<String, Biome>()
            .addTransform(id -> BuiltinRegistries.BIOME.get(Identifier.tryParse(id)), new NotNullValidator<>())
            .addTransform(id -> BuiltinRegistries.BIOME.get(Identifier.tryParse("minecraft:" + id.toLowerCase())), new NotNullValidator<>()).build();

    @Override
    public void register(TypeRegistry registry) {
        genericLoaders.register(registry);
        registry
                .registerLoader(BlockData.class, (t, o, l) -> worldHandle.createBlockData((String) o))
                .registerLoader(MaterialData.class, (t, o, l) -> worldHandle.createMaterialData((String) o))
                .registerLoader(com.dfsek.terra.api.platform.world.Biome.class, (t, o, l) -> new FabricBiome(biomeFixer.translate((String) o)));
    }

    public static String createBiomeID(ConfigPack pack, TerraBiome biome) {
        return pack.getTemplate().getID().toLowerCase() + "/" + biome.getID().toLowerCase();
    }

    private Biome createBiome(TerraBiome biome) {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 5, 100);

        Biome vanilla = ((FabricBiome) new ArrayList<>(biome.getVanillaBiomes().getContents()).get(0)).getHandle();

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        generationSettings.surfaceBuilder(SurfaceBuilder.DEFAULT.withConfig(new TernarySurfaceConfig(Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.GRAVEL.getDefaultState()))); // It needs a surfacebuilder, even though we dont use it.
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, POPULATOR_CONFIGURED_FEATURE);

        BiomeEffects.Builder effects = new BiomeEffects.Builder()
                .waterColor(vanilla.getWaterColor())
                .waterFogColor(vanilla.getWaterFogColor())
                .fogColor(vanilla.getFogColor())
                .skyColor(vanilla.getSkyColor())
                .grassColorModifier(vanilla.getEffects().getGrassColorModifier());
        if(vanilla.getEffects().getGrassColor().isPresent()) {
            effects.grassColor(vanilla.getEffects().getGrassColor().get());
        }
        if(vanilla.getEffects().getFoliageColor().isPresent()) {
            effects.foliageColor(vanilla.getEffects().getFoliageColor().get());
        }

        return (new Biome.Builder())
                .precipitation(vanilla.getPrecipitation())
                .category(vanilla.getCategory())
                .depth(vanilla.getDepth())
                .scale(vanilla.getScale())
                .temperature(vanilla.getTemperature())
                .downfall(vanilla.getDownfall())
                .effects(vanilla.getEffects()) // TODO: configurable
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }

    private static ConfiguredFeature<?, ?> getFeature(String name) {
        Class<ConfiguredFeatures> featuresClass = ConfiguredFeatures.class;
        Field feature;
        try {
            feature = featuresClass.getField(name);
            return (ConfiguredFeature<?, ?>) feature.get(null);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("No such feature: " + name);
        }
    }

    @Override
    public void onInitialize() {
        logger.setLevel(Level.INFO);
        instance = this;

        config = new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra");
        saveDefaultConfig();
        plugin.load(this);
        LangUtil.load("en_us", this);
        logger.info("Initializing Terra...");

        registry.loadAll(this);

        Registry.register(Registry.FEATURE, new Identifier("terra", "flora_populator"), POPULATOR_FEATURE);
        RegistryKey<ConfiguredFeature<?, ?>> floraKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("terra", "flora_populator"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, floraKey.getValue(), POPULATOR_CONFIGURED_FEATURE);

        registry.forEach(pack -> pack.getBiomeRegistry().forEach(biome -> Registry.register(BuiltinRegistries.BIOME, new Identifier("terra", createBiomeID(pack, biome)), createBiome(biome)))); // Register all Terra biomes.
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("terra:terra"), FabricChunkGeneratorWrapper.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new Identifier("terra:terra"), TerraBiomeSource.CODEC);

        if(FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            GeneratorTypeAccessor.getValues().add(new GeneratorType("terra") {
                @Override
                protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
                    ConfigPack pack = registry.get("DEFAULT");
                    return new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
                }
            });
        }

    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }
}
