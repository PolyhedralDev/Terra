package com.dfsek.terra.mod.util;

import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;
import com.dfsek.terra.mod.generation.TerraBiomeSource;


public class PresetUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PresetUtil.class);
    private static final List<Identifier> PRESETS = new ArrayList<>();
    
    public static Pair<Identifier, WorldPreset> createDefault(ConfigPack pack) {
        Registry<DimensionType> dimensionTypeRegistry = BuiltinRegistries.DIMENSION_TYPE;
        Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = BuiltinRegistries.CHUNK_GENERATOR_SETTINGS;
        Registry<StructureSet> structureSetRegistry = BuiltinRegistries.STRUCTURE_SET;
        Registry<NoiseParameters> noiseParametersRegistry = BuiltinRegistries.NOISE_PARAMETERS;
        Registry<Biome> biomeRegistry = BuiltinRegistries.BIOME;
        
        RegistryEntry<DimensionType> theNetherDimensionType = dimensionTypeRegistry.getOrCreateEntry(DimensionTypes.THE_NETHER);
        RegistryEntry<ChunkGeneratorSettings>
                netherChunkGeneratorSettings = chunkGeneratorSettingsRegistry.getOrCreateEntry(ChunkGeneratorSettings.NETHER);
        DimensionOptions netherDimensionOptions = new DimensionOptions(theNetherDimensionType,
                                                                       new NoiseChunkGenerator(structureSetRegistry,
                                                                                               noiseParametersRegistry,
                                                                                               MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(
                                                                                                       biomeRegistry),
                                                                                               netherChunkGeneratorSettings));
        RegistryEntry<DimensionType> theEndDimensionType = dimensionTypeRegistry.getOrCreateEntry(DimensionTypes.THE_END);
        RegistryEntry<ChunkGeneratorSettings> endChunkGeneratorSettings = chunkGeneratorSettingsRegistry.getOrCreateEntry(
                ChunkGeneratorSettings.END);
        DimensionOptions endDimensionOptions = new DimensionOptions(theEndDimensionType,
                                                                    new NoiseChunkGenerator(structureSetRegistry, noiseParametersRegistry,
                                                                                            new TheEndBiomeSource(biomeRegistry),
                                                                                            endChunkGeneratorSettings));
        
        RegistryEntry<DimensionType> overworldDimensionType = dimensionTypeRegistry.getOrCreateEntry(DimensionTypes.OVERWORLD);
        
        RegistryEntry<ChunkGeneratorSettings> overworld = chunkGeneratorSettingsRegistry.getOrCreateEntry(ChunkGeneratorSettings.OVERWORLD);
        
        Identifier generatorID = Identifier.of("terra", pack.getID().toLowerCase(Locale.ROOT) + "/" + pack.getNamespace().toLowerCase(
                Locale.ROOT));
        
        PRESETS.add(generatorID);
        
        TerraBiomeSource biomeSource = new TerraBiomeSource(biomeRegistry, pack);
        ChunkGenerator generator = new MinecraftChunkGeneratorWrapper(structureSetRegistry, biomeSource, pack, overworld);
        
        DimensionOptions dimensionOptions = new DimensionOptions(overworldDimensionType, generator);
        WorldPreset preset = new WorldPreset(
                Map.of(
                        DimensionOptions.OVERWORLD, dimensionOptions,
                        DimensionOptions.NETHER, netherDimensionOptions,
                        DimensionOptions.END, endDimensionOptions
                      )
        );
        LOGGER.info("Created world type \"{}\"", generatorID);
        return Pair.of(generatorID, preset);
    }
    
    public static List<Identifier> getPresets() {
        return PRESETS;
    }
}
