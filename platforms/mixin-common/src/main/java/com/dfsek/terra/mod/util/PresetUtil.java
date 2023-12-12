package com.dfsek.terra.mod.util;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
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
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;
import com.dfsek.terra.mod.generation.TerraBiomeSource;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.mod.ModPlatform;
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;
import com.dfsek.terra.mod.generation.TerraBiomeSource;




public class PresetUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PresetUtil.class);
    private static final List<Identifier> PRESETS = new ArrayList<>();

    public static Pair<Identifier, WorldPreset> createDefault(ConfigPack pack, ModPlatform platform) {
        Registry<DimensionType> dimensionTypeRegistry = platform.dimensionTypeRegistry();
        Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = platform.chunkGeneratorSettingsRegistry();
        Registry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists =
            platform.multiNoiseBiomeSourceParameterListRegistry();


        RegistryEntry<DimensionType> overworldDimensionType = dimensionTypeRegistry.getEntry(DimensionTypes.OVERWORLD).orElseThrow();
        RegistryEntry<ChunkGeneratorSettings> overworld = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.OVERWORLD)
            .orElseThrow();


        Identifier generatorID = Identifier.of("terra", pack.getID().toLowerCase(Locale.ROOT) + "/" + pack.getNamespace().toLowerCase(
            Locale.ROOT));

        PRESETS.add(generatorID);

        TerraBiomeSource biomeSource = new TerraBiomeSource(pack);
        ChunkGenerator generator = new MinecraftChunkGeneratorWrapper(biomeSource, pack, overworld);

        DimensionOptions dimensionOptions = new DimensionOptions(overworldDimensionType, generator);

        HashMap<RegistryKey<DimensionOptions>, DimensionOptions> dimensionMap = new HashMap<>();

        dimensionMap.put(DimensionOptions.OVERWORLD, dimensionOptions);

        insertDefaults(dimensionTypeRegistry, chunkGeneratorSettingsRegistry, multiNoiseBiomeSourceParameterLists, platform.biomeRegistry(), dimensionMap);

        WorldPreset preset = new WorldPreset(dimensionMap);
        LOGGER.info("Created world type \"{}\"", generatorID);
        return Pair.of(generatorID, preset);
    }

    public static Pair<Identifier, WorldPreset> createMetaPackPreset(MetaPack metaPack, ModPlatform platform) {
        Registry<DimensionType> dimensionTypeRegistry = platform.dimensionTypeRegistry();
        Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = platform.chunkGeneratorSettingsRegistry();
        Registry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists =
            platform.multiNoiseBiomeSourceParameterListRegistry();

        Identifier generatorID = Identifier.of("terra", metaPack.getID().toLowerCase(Locale.ROOT) + "/" + metaPack.getNamespace().toLowerCase(
            Locale.ROOT));

        PRESETS.add(generatorID);

        HashMap<RegistryKey<DimensionOptions>, DimensionOptions> dimensionMap = new HashMap<>();

        metaPack.packs().forEach((key, pack) -> {
            Identifier demensionIdentifier = new Identifier(key);
            DimensionType dimensionType = DimensionUtil.createDimension(pack, platform);
            RegistryEntry<DimensionType> dimensionTypeRegistryEntry = dimensionTypeRegistry.getEntry(dimensionType);

            TerraBiomeSource biomeSource = new TerraBiomeSource(pack);

            RegistryEntry<ChunkGeneratorSettings> generatorSettings = chunkGeneratorSettingsRegistry.getEntry(chunkGeneratorSettingsRegistry.get(demensionIdentifier));
            if (key.equals("minecraft:the_nether") || key.equals("minecraft:the_end")) { //TODO REMOVE WHEN ADDING CUSTOM GEN SETTINGS
                Identifier demensionIdentifier2 = new Identifier(key.replace("the_", ""));
                generatorSettings = chunkGeneratorSettingsRegistry.getEntry(chunkGeneratorSettingsRegistry.get(demensionIdentifier2));
            }

            ChunkGenerator generator = new MinecraftChunkGeneratorWrapper(biomeSource, pack, generatorSettings);

            DimensionOptions dimensionOptions = new DimensionOptions(dimensionTypeRegistryEntry, generator);
            RegistryKey<DimensionOptions> dimensionOptionsRegistryKey = RegistryKey.of(RegistryKeys.DIMENSION, demensionIdentifier);
            dimensionMap.put(dimensionOptionsRegistryKey, dimensionOptions);
        });

        insertDefaults(dimensionTypeRegistry, chunkGeneratorSettingsRegistry, multiNoiseBiomeSourceParameterLists, platform.biomeRegistry(), dimensionMap);

        WorldPreset preset = new WorldPreset(dimensionMap);
        LOGGER.info("Created world type \"{}\"", generatorID);
        return Pair.of(generatorID, preset);
    }

    private static void insertDefaults(Registry<DimensionType> dimensionTypeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, Registry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists, Registry<Biome> biomeRegistry, HashMap<RegistryKey<DimensionOptions>, DimensionOptions> map) {
        if (!map.containsKey(DimensionOptions.OVERWORLD)) {
            RegistryEntry<DimensionType> overworldDimensionType = dimensionTypeRegistry.getEntry(DimensionTypes.OVERWORLD).orElseThrow();

            RegistryEntry.Reference<MultiNoiseBiomeSourceParameterList> overworldChunkBiomeReference = multiNoiseBiomeSourceParameterLists.getEntry(
                MultiNoiseBiomeSourceParameterLists.OVERWORLD).orElseThrow();

            RegistryEntry<ChunkGeneratorSettings> overworldChunkGeneratorSettings = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.OVERWORLD)
                .orElseThrow();


            DimensionOptions overworldDimensionOptions = new DimensionOptions(overworldDimensionType, (new NoiseChunkGenerator(MultiNoiseBiomeSource.create(overworldChunkBiomeReference), overworldChunkGeneratorSettings)));
            map.put(DimensionOptions.OVERWORLD, overworldDimensionOptions);
        }
        if (!map.containsKey(DimensionOptions.NETHER)) {
            RegistryEntry<DimensionType> netherDimensionType = dimensionTypeRegistry.getEntry(DimensionTypes.THE_NETHER).orElseThrow();

            RegistryEntry.Reference<MultiNoiseBiomeSourceParameterList> netherChunkBiomeReference = multiNoiseBiomeSourceParameterLists.getEntry(
                MultiNoiseBiomeSourceParameterLists.NETHER).orElseThrow();

            RegistryEntry<ChunkGeneratorSettings> netherChunkGeneratorSettings = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.NETHER)
                .orElseThrow();


            DimensionOptions overworldDimensionOptions = new DimensionOptions(netherDimensionType, (new NoiseChunkGenerator(MultiNoiseBiomeSource.create(netherChunkBiomeReference), netherChunkGeneratorSettings)));
            map.put(DimensionOptions.NETHER, overworldDimensionOptions);
        }
        if (!map.containsKey(DimensionOptions.END)) {
            RegistryEntry<DimensionType> endDimensionType = dimensionTypeRegistry.getEntry(DimensionTypes.THE_END).orElseThrow();

            RegistryEntry<ChunkGeneratorSettings> endChunkGeneratorSettings = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.END)
                .orElseThrow();


            DimensionOptions overworldDimensionOptions = new DimensionOptions(endDimensionType, (new NoiseChunkGenerator(TheEndBiomeSource.createVanilla(biomeRegistry.getReadOnlyWrapper()), endChunkGeneratorSettings)));
            map.put(DimensionOptions.END, overworldDimensionOptions);
        }
    }

    public static List<Identifier> getPresets() {
        return PRESETS;
    }
}
