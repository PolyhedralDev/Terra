package com.dfsek.terra.mod.util;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
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
    
    public static Pair<Identifier, WorldPreset> createDefault(ConfigPack pack, DynamicRegistryManager registryManager) {
        Registry<DimensionType> dimensionTypeRegistry = registryManager.get(RegistryKeys.DIMENSION_TYPE);
        Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = registryManager.get(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
        Registry<Biome> biomeRegistry = registryManager.get(RegistryKeys.BIOME);
        
        RegistryEntry<DimensionType> theNetherDimensionType = dimensionTypeRegistry.getEntry(DimensionTypes.THE_NETHER).orElseThrow();
        RegistryEntry<ChunkGeneratorSettings>
                netherChunkGeneratorSettings = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.NETHER).orElseThrow();
        DimensionOptions netherDimensionOptions = new DimensionOptions(theNetherDimensionType,
                                                                       new NoiseChunkGenerator(
                                                                               MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(
                                                                                       registryManager.createRegistryLookup()
                                                                                                      .getOrThrow(RegistryKeys.BIOME)),
                                                                               netherChunkGeneratorSettings));
        RegistryEntry<DimensionType> theEndDimensionType = dimensionTypeRegistry.getEntry(DimensionTypes.THE_END).orElseThrow();
        RegistryEntry<ChunkGeneratorSettings> endChunkGeneratorSettings = chunkGeneratorSettingsRegistry.getEntry(
                ChunkGeneratorSettings.END).orElseThrow();
        DimensionOptions endDimensionOptions = new DimensionOptions(theEndDimensionType,
                                                                    new NoiseChunkGenerator(
                                                                            TheEndBiomeSource.createVanilla(
                                                                                    registryManager.createRegistryLookup()
                                                                                                   .getOrThrow(RegistryKeys.BIOME)),
                                                                            endChunkGeneratorSettings));
        
        RegistryEntry<DimensionType> overworldDimensionType = dimensionTypeRegistry.getEntry(DimensionTypes.OVERWORLD).orElseThrow();
        
        RegistryEntry<ChunkGeneratorSettings> overworld = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.OVERWORLD)
                                                                                        .orElseThrow();
        
        Identifier generatorID = Identifier.of("terra", pack.getID().toLowerCase(Locale.ROOT) + "/" + pack.getNamespace().toLowerCase(
                Locale.ROOT));
        
        PRESETS.add(generatorID);
        
        TerraBiomeSource biomeSource = new TerraBiomeSource(biomeRegistry, pack);
        ChunkGenerator generator = new MinecraftChunkGeneratorWrapper(biomeSource, pack, overworld);
        
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
