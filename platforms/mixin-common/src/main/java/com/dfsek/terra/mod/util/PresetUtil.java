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
        
        RegistryEntry<DimensionType> overworldDimensionType = dimensionTypeRegistry.getEntry(DimensionTypes.OVERWORLD).orElseThrow();
        RegistryEntry<ChunkGeneratorSettings> overworld = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.OVERWORLD)
                                                                                        .orElseThrow();
        
        Identifier generatorID = Identifier.of("terra", pack.getID().toLowerCase(Locale.ROOT) + "/" + pack.getNamespace().toLowerCase(
                Locale.ROOT));
        
        PRESETS.add(generatorID);
        
        TerraBiomeSource biomeSource = new TerraBiomeSource(pack);
        ChunkGenerator generator = new MinecraftChunkGeneratorWrapper(biomeSource, pack, overworld);
        
        DimensionOptions dimensionOptions = new DimensionOptions(overworldDimensionType, generator);
        WorldPreset preset = ((WorldPresetsRegistrarAccessor) RegistrarInstance.INSTANCE).callCreatePreset(dimensionOptions);
        LOGGER.info("Created world type \"{}\"", generatorID);
        return Pair.of(generatorID, preset);
    }
    
    public static List<Identifier> getPresets() {
        return PRESETS;
    }
}
