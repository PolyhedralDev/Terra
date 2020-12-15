package com.dfsek.terra.fabric;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.GenericLoaders;
import com.dfsek.terra.api.gaea.lang.Language;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.inventory.ItemHandle;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.api.translator.MapTransform;
import com.dfsek.terra.api.translator.Transformer;
import com.dfsek.terra.config.base.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.fabric.inventory.FabricItemHandle;
import com.dfsek.terra.fabric.mixin.GeneratorTypeAccessor;
import com.dfsek.terra.fabric.world.FabricBiome;
import com.dfsek.terra.fabric.world.FabricWorldHandle;
import com.dfsek.terra.fabric.world.TerraBiomeSource;
import com.dfsek.terra.fabric.world.generator.FabricChunkGeneratorWrapper;
import com.dfsek.terra.registry.ConfigRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.TreeFeature;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TerraFabricPlugin implements TerraPlugin, ModInitializer {

    private final Map<Long, TerraWorld> worldMap = new HashMap<>();
    private static TerraFabricPlugin instance;

    public static TerraFabricPlugin getInstance() {
        return instance;
    }

    private final GenericLoaders genericLoaders = new GenericLoaders(this);
    private final Logger logger = Logger.getLogger("Terra");
    private final ItemHandle itemHandle = new FabricItemHandle();
    private final WorldHandle worldHandle = new FabricWorldHandle();
    private final ConfigRegistry registry = new ConfigRegistry();
    private File config;
    private final PluginConfig plugin;

    {
        logger.setLevel(Level.INFO);
        plugin = new PluginConfig();
    }

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
            return new Language(new File(getDataFolder(), "lang/en_us/yml"));
        } catch(IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ConfigRegistry getRegistry() {
        return registry;
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
            FileUtils.copyInputStreamToFile(stream, new File(getDataFolder(), "config.yml"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(TypeRegistry registry) {
        genericLoaders.register(registry);
        registry
                .registerLoader(BlockData.class, (t, o, l) -> worldHandle.createBlockData((String) o))
                .registerLoader(MaterialData.class, (t, o, l) -> worldHandle.createMaterialData((String) o))
                .registerLoader(com.dfsek.terra.api.generic.world.Biome.class, (t, o, l) -> {
                    String id = (String) o;
                    if(!id.contains(":")) id = "minecraft:" + id.toLowerCase();
                    Identifier identifier = new Identifier(id);
                    Biome biome = BuiltinRegistries.BIOME.get(identifier);
                    return new FabricBiome(biome);
                });
    }

    @Override
    public void onInitialize() {
        instance = this;

        Map<Identifier, Feature<?>> treeFeatureMap = new HashMap<>();
        BuiltinRegistries.CONFIGURED_FEATURE.stream().filter(feature ->
                feature.feature instanceof TreeFeature
                        || feature.feature instanceof HugeMushroomFeature).forEach(tree -> System.out.println(BuiltinRegistries.CONFIGURED_FEATURE.getId(tree)));

        Transformer<String, ConfiguredFeature<?, ?>> treeTransformer = new Transformer.Builder<String, ConfiguredFeature<?, ?>>()
                .addTransform(id -> BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse(id)))
                .addTransform(new MapTransform<String, ConfiguredFeature<?, ?>>()
                        .add("BROWN_MUSHROOM", BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:huge_brown_mushroom")))
                        .add("RED_MUSHROOM", BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:huge_red_mushroom")))
                        .add("JUNGLE", BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:mega_jungle_tree")))
                        .add("JUNGLE_COCOA", BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:jungle_tree_no_vine")))
                        .add("LARGE_OAK", BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:fancy_oak")))
                        .add("LARGE_SPRUCE", BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:pine")))
                        .add("SMALL_JUNGLE", BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:jungle_tree")))
                        .add("SWAMP_OAK", BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:oak")))
                        .add("TALL_BIRCH", BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:birch"))))
                .addTransform(id -> BuiltinRegistries.CONFIGURED_FEATURE.get(Identifier.tryParse("minecraft:" + id.toLowerCase()))).build();
        ((FabricWorldHandle) worldHandle).setTreeTransformer(treeTransformer);

        config = new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra");
        saveDefaultConfig();
        plugin.load(this);
        LangUtil.load("en_us", this);
        logger.info("Initializing Terra...");
        registry.loadAll(this);


        /*
        registry.forEach(config -> {
            String pack = config.getTemplate().getID().toLowerCase();
            config.getBiomeRegistry().forEach(terraBiome -> {
                Biome biome = (new Biome.Builder()).build();
                Registry.register(BuiltinRegistries.BIOME, new Identifier("terra",  pack + "_" + terraBiome.getID().toLowerCase()), biome);
            });
        });
         */

        if(FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            GeneratorTypeAccessor.getValues().add(new GeneratorType("terra") {
                @Override
                protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
                    return new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed), seed, registry.get("DEFAULT"));
                }
            });
        }
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("terra:terra"), FabricChunkGeneratorWrapper.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new Identifier("terra:terra"), TerraBiomeSource.CODEC);
    }
}
