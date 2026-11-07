package com.dfsek.terra.bukkit.nms.v26_1;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction.SinglePointContext;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.AttributeModifier;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.bukkit.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.bukkit.nms.NMSStructureBeardifier;
import com.dfsek.terra.bukkit.nms.NMSVersionBindings;
import com.dfsek.terra.bukkit.nms.config.VanillaBiomeProperties;


public final class Bindings implements NMSVersionBindings {
    @Override
    public void addSpawn(MobSpawnSettings.Builder builder, MobCategory category, EntityType<?> type, int weight,
                         int minCount, int maxCount) {
        builder.addSpawn(category, weight, new SpawnerData(type, minCount, maxCount));
    }

    @Override
    public void addSpawnCost(MobSpawnSettings.Builder builder, EntityType<?> type, double mass, double gravity) {
        builder.addMobCharge(type, mass, gravity);
    }

    @Override
    public boolean setCreatureGenerationProbability(MobSpawnSettings.Builder builder, float probability) {
        builder.creatureGenerationProbability(probability);
        return true;
    }

    @Override
    public void copyVanillaSpawnSettings(Biome.BiomeBuilder builder, Biome biome) {
        builder.mobSpawnSettings(biome.getMobSettings());
    }

    @Override
    public void setSpawnSettings(Biome.BiomeBuilder builder, MobSpawnSettings settings) {
        builder.mobSpawnSettings(settings);
    }

    @Override
    public void applyBiomeColorOverrides(Biome.BiomeBuilder builder, VanillaBiomeProperties properties) {
        if(properties.getFogColor() != null) {
            builder.modifyAttribute(EnvironmentAttributes.FOG_COLOR, AttributeModifier.override(), properties.getFogColor());
        }
        if(properties.getWaterFogColor() != null) {
            builder.modifyAttribute(EnvironmentAttributes.WATER_FOG_COLOR, AttributeModifier.override(), properties.getWaterFogColor());
        }
        if(properties.getSkyColor() != null) {
            builder.modifyAttribute(EnvironmentAttributes.SKY_COLOR, AttributeModifier.override(), properties.getSkyColor());
        }
    }

    @Override
    public void applyStructureBeard(ChunkGenerator generator, StructureManager structures, ChunkAccess chunk, WorldProperties world,
                                    BiomeProvider biomeProvider, PreLoadCompatibilityOptions options) {
        Beardifier beardifier = Beardifier.forStructuresInChunk(structures, chunk.getPos());
        NMSStructureBeardifier.apply(generator, chunk, world, biomeProvider, options,
            (x, y, z) -> beardifier.compute(new SinglePointContext(x, y, z)));
    }
}
