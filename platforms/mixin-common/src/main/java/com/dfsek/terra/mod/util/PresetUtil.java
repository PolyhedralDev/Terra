package com.dfsek.terra.mod.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.MetaPack;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.mod.ModPlatform;
import com.dfsek.terra.mod.attribute.AttributeReport;
import com.dfsek.terra.mod.attribute.MinecraftAttributeParents;
import com.dfsek.terra.mod.attribute.MinecraftDimensionFactory;
import com.dfsek.terra.mod.attribute.MinecraftTimelines;
import com.dfsek.terra.mod.attribute.PackParents;
import com.dfsek.terra.mod.attribute.VanillaContext;
import com.dfsek.terra.mod.generation.GenerationSettings;
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;
import com.dfsek.terra.mod.generation.TerraBiomeSource;


public class PresetUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PresetUtil.class);
    private static final List<Pair<Identifier, Boolean>> PRESETS = new ArrayList<>();

    public static Pair<Identifier, WorldPreset> createDefault(ConfigPack pack, ModPlatform platform, boolean extended,
                                                              boolean packInMetapack) {
        Registry<DimensionType> dimensionTypeRegistry = platform.dimensionTypeRegistry();
        Registry<NoiseGeneratorSettings> chunkGeneratorSettingsRegistry = platform.chunkGeneratorSettingsRegistry();
        Registry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists =
            platform.multiNoiseBiomeSourceParameterListRegistry();


        Identifier generatorID = Identifier.tryParse(
            "terra:" + pack.getID().toLowerCase(Locale.ROOT) + "/" + pack.getNamespace().toLowerCase(
                Locale.ROOT));

        PRESETS.add(Pair.of(generatorID, extended));

        HashMap<ResourceKey<LevelStem>, LevelStem> dimensionMap = new HashMap<>();

        insertCustom(platform, "minecraft:overworld", pack, dimensionTypeRegistry, dimensionMap, packInMetapack);

        insertDefaults(dimensionTypeRegistry, chunkGeneratorSettingsRegistry, multiNoiseBiomeSourceParameterLists, platform.biomeRegistry(),
            dimensionMap);

        WorldPreset preset = new WorldPreset(dimensionMap);
        LOGGER.info("Created world type \"{}\"", generatorID);
        return Pair.of(generatorID, preset);
    }

    public static Pair<Identifier, WorldPreset> createMetaPackPreset(MetaPack metaPack, ModPlatform platform, boolean extended) {
        Registry<DimensionType> dimensionTypeRegistry = platform.dimensionTypeRegistry();
        Registry<NoiseGeneratorSettings> chunkGeneratorSettingsRegistry = platform.chunkGeneratorSettingsRegistry();
        Registry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists =
            platform.multiNoiseBiomeSourceParameterListRegistry();

        Identifier generatorID = Identifier.fromNamespaceAndPath("terra",
            metaPack.getID().toLowerCase(Locale.ROOT) + "/" + metaPack.getNamespace().toLowerCase(
                Locale.ROOT));

        PRESETS.add(Pair.of(generatorID, extended));

        HashMap<ResourceKey<LevelStem>, LevelStem> dimensionMap = new HashMap<>();

        metaPack.packs().forEach((key, pack) -> {
            insertCustom(platform, key, pack, dimensionTypeRegistry, dimensionMap, false);
        });

        insertDefaults(dimensionTypeRegistry, chunkGeneratorSettingsRegistry, multiNoiseBiomeSourceParameterLists, platform.biomeRegistry(),
            dimensionMap);

        WorldPreset preset = new WorldPreset(dimensionMap);
        LOGGER.info("Created world type \"{}\"", generatorID);
        return Pair.of(generatorID, preset);
    }

    private static void insertCustom(ModPlatform platform, String key, ConfigPack pack, Registry<DimensionType> dimensionTypeRegistry,
                                     HashMap<ResourceKey<LevelStem>, LevelStem> dimensionMap, boolean packInMetapack) {
        Identifier demensionIdentifier = Identifier.parse(key);

        MinecraftAttributeParents parents = platform.attributeParents();
        VanillaContext context = parents.context();
        if(context == null) throw new IllegalStateException("no registry load has begun");
        PackParents packParents = parents.prepare(pack, platform.getAttributeHandle().pack(pack), AttributeReport.logging(pack));
        MinecraftDimensionFactory factory = new MinecraftDimensionFactory(platform.getAttributeHandle(), context);

        Identifier dimensionTypeID = Identifier.fromNamespaceAndPath("terra", pack.getID().toLowerCase(Locale.ROOT));

        DimensionType dimensionType;
        if(!packInMetapack) {
            dimensionType = factory.create(packParents, MinecraftTimelines.tagFor(pack, packParents));
            ResourceKey<DimensionType> dimensionTypeRegistryKey = MinecraftUtil.registerDimensionTypeKey(
                dimensionTypeID);

            Registry.registerForHolder(dimensionTypeRegistry, dimensionTypeRegistryKey, dimensionType);
        } else {
            dimensionType = dimensionTypeRegistry.getValue(dimensionTypeID);
        }

        Holder<DimensionType> dimensionTypeRegistryEntry = dimensionTypeRegistry.wrapAsHolder(dimensionType);

        TerraBiomeSource biomeSource = new TerraBiomeSource(pack);

        GenerationSettings generatorSettings = factory.generationSettings(packParents, dimensionType);

        ChunkGenerator generator = new MinecraftChunkGeneratorWrapper(biomeSource, pack, generatorSettings);

        LevelStem dimensionOptions = new LevelStem(dimensionTypeRegistryEntry, generator);
        ResourceKey<LevelStem> dimensionOptionsRegistryKey = ResourceKey.create(Registries.LEVEL_STEM, demensionIdentifier);
        dimensionMap.put(dimensionOptionsRegistryKey, dimensionOptions);
    }

    private static void insertDefaults(Registry<DimensionType> dimensionTypeRegistry,
                                       Registry<NoiseGeneratorSettings> chunkGeneratorSettingsRegistry,
                                       Registry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists,
                                       Registry<Biome> biomeRegistry, HashMap<ResourceKey<LevelStem>, LevelStem> map) {
        if(!map.containsKey(LevelStem.OVERWORLD)) {
            Holder<DimensionType> overworldDimensionType = dimensionTypeRegistry.wrapAsHolder(
                dimensionTypeRegistry.getValue(BuiltinDimensionTypes.OVERWORLD));

            Holder<MultiNoiseBiomeSourceParameterList> overworldChunkBiomeReference =
                multiNoiseBiomeSourceParameterLists.wrapAsHolder(multiNoiseBiomeSourceParameterLists.getValue(
                    MultiNoiseBiomeSourceParameterLists.OVERWORLD));

            Holder<NoiseGeneratorSettings> overworldChunkGeneratorSettings = chunkGeneratorSettingsRegistry.wrapAsHolder(
                chunkGeneratorSettingsRegistry.getValue(
                    NoiseGeneratorSettings.OVERWORLD));


            LevelStem overworldDimensionOptions = new LevelStem(overworldDimensionType,
                (new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromPreset(overworldChunkBiomeReference), overworldChunkGeneratorSettings)));
            map.put(LevelStem.OVERWORLD, overworldDimensionOptions);
        }
        if(!map.containsKey(LevelStem.NETHER)) {
            Holder<DimensionType> netherDimensionType = dimensionTypeRegistry.wrapAsHolder(
                dimensionTypeRegistry.getValue(BuiltinDimensionTypes.NETHER));

            Holder<MultiNoiseBiomeSourceParameterList> netherChunkBiomeReference =
                multiNoiseBiomeSourceParameterLists.wrapAsHolder(multiNoiseBiomeSourceParameterLists.getValue(
                    MultiNoiseBiomeSourceParameterLists.NETHER));

            Holder<NoiseGeneratorSettings> netherChunkGeneratorSettings = chunkGeneratorSettingsRegistry.wrapAsHolder(
                chunkGeneratorSettingsRegistry.getValue(
                    NoiseGeneratorSettings.NETHER));

            LevelStem overworldDimensionOptions = new LevelStem(netherDimensionType,
                (new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromPreset(netherChunkBiomeReference), netherChunkGeneratorSettings)));
            map.put(LevelStem.NETHER, overworldDimensionOptions);
        }
        if(!map.containsKey(LevelStem.END)) {
            Holder<DimensionType> endDimensionType = dimensionTypeRegistry.wrapAsHolder(
                dimensionTypeRegistry.getValue(BuiltinDimensionTypes.END));

            Holder<NoiseGeneratorSettings> endChunkGeneratorSettings = chunkGeneratorSettingsRegistry.wrapAsHolder(
                chunkGeneratorSettingsRegistry.getValue(
                    NoiseGeneratorSettings.END));


            LevelStem overworldDimensionOptions = new LevelStem(endDimensionType,
                (new NoiseBasedChunkGenerator(TheEndBiomeSource.create(biomeRegistry), endChunkGeneratorSettings)));
            map.put(LevelStem.END, overworldDimensionOptions);
        }
    }

    public static List<Pair<Identifier, Boolean>> getPresets() {
        return PRESETS;
    }
}
