package com.dfsek.terra.mod.util;

import com.dfsek.terra.mod.ModPlatform;

import com.dfsek.terra.mod.mixin.access.WorldPresetsRegistrarAccessor;
import com.dfsek.terra.mod.mixin_ifaces.RegistrarInstance;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
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
        
        RegistryEntry<DimensionType> registryEntry = dimensionTypeRegistry.getEntry(DimensionTypes.THE_NETHER).orElseThrow();
        RegistryEntry.Reference<MultiNoiseBiomeSourceParameterList> reference = multiNoiseBiomeSourceParameterLists.getEntry(MultiNoiseBiomeSourceParameterLists.NETHER).orElseThrow();
        RegistryEntry<ChunkGeneratorSettings> registryEntry2 = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.NETHER).orElseThrow();
        
        RegistryEntry<DimensionType> registryEntry3 = dimensionTypeRegistry.getEntry(DimensionTypes.THE_END).orElseThrow();
        RegistryEntry<ChunkGeneratorSettings> registryEntry4 = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.END).orElseThrow();
        
        TerraBiomeSource biomeSource = new TerraBiomeSource(pack);
        ChunkGenerator generator = new MinecraftChunkGeneratorWrapper(biomeSource, pack, overworld);
        
        DimensionOptions dimensionOptions = new DimensionOptions(overworldDimensionType, generator);
        DimensionOptions netherDimensionOptions = new DimensionOptions(registryEntry, new NoiseChunkGenerator(MultiNoiseBiomeSource.create(reference), registryEntry2));
        DimensionOptions endDimensionOptions = new DimensionOptions(registryEntry3, new NoiseChunkGenerator(TheEndBiomeSource.createVanilla(platform.biomeRegistry().getReadOnlyWrapper()), registryEntry4));
        
        WorldPreset preset = createPreset(dimensionOptions, netherDimensionOptions, endDimensionOptions);
        LOGGER.info("Created world type \"{}\"", generatorID);
        return Pair.of(generatorID, preset);
    }
    
    private static WorldPreset createPreset(DimensionOptions dimensionOptions, DimensionOptions netherDimensionOptions, DimensionOptions endDimensionOptions) {
        return new WorldPreset(
                Map.of(DimensionOptions.OVERWORLD, dimensionOptions, DimensionOptions.NETHER, netherDimensionOptions, DimensionOptions.END, endDimensionOptions)
        );
    }
    
    public static List<Identifier> getPresets() {
        return PRESETS;
    }
}
