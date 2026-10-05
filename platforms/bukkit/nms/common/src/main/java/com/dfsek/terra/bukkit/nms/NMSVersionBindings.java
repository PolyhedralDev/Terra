package com.dfsek.terra.bukkit.nms;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkAccess;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.bukkit.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.bukkit.nms.config.VanillaBiomeProperties;


public interface NMSVersionBindings {
    default NMSBiomeProvider createBiomeProvider(BiomeProvider delegate, long seed) {
        return new NMSBiomeProvider(delegate, seed);
    }

    default NMSChunkGeneratorDelegate createChunkGenerator(net.minecraft.world.level.chunk.ChunkGenerator vanilla,
                                                           ConfigPack pack,
                                                           NMSBiomeProvider biomeProvider, long seed) {
        return new NMSChunkGeneratorDelegate(vanilla, pack, biomeProvider, seed, this);
    }

    void addSpawn(MobSpawnSettings.Builder builder, MobCategory category, EntityType<?> type, int weight, int minCount, int maxCount);

    void addSpawnCost(MobSpawnSettings.Builder builder, EntityType<?> type, double mass, double gravity);

    boolean setCreatureGenerationProbability(MobSpawnSettings.Builder builder, float probability);

    void copyVanillaSpawnSettings(BiomeBuilder builder, Biome biome);

    void setSpawnSettings(BiomeBuilder builder, MobSpawnSettings settings);

    void applyBiomeColorOverrides(BiomeBuilder builder, VanillaBiomeProperties properties);

    void applyStructureBeard(ChunkGenerator generator, StructureManager structures, ChunkAccess chunk, WorldProperties world,
                             BiomeProvider biomeProvider, PreLoadCompatibilityOptions options);
}
