package com.dfsek.terra.bukkit.nms.v26_3;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.AttributeModifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.bukkit.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.bukkit.nms.NMSBiomeProvider;
import com.dfsek.terra.bukkit.nms.NMSChunkGeneratorDelegate;
import com.dfsek.terra.bukkit.nms.NMSStructureBeardifier;
import com.dfsek.terra.bukkit.nms.NMSVersionBindings;
import com.dfsek.terra.bukkit.nms.config.VanillaBiomeProperties;


public final class Bindings implements NMSVersionBindings {
    @Override
    public NMSBiomeProvider createBiomeProvider(BiomeProvider delegate, long seed) {
        return new BiomeProvider26_3(delegate, seed);
    }

    @Override
    public NMSChunkGeneratorDelegate createChunkGenerator(
        net.minecraft.world.level.chunk.ChunkGenerator vanilla, ConfigPack pack,
        NMSBiomeProvider biomeProvider, long seed) {
        return new ChunkGenerator26_3(vanilla, pack, biomeProvider, seed, this);
    }

    @Override
    public void addSpawn(MobSpawnSettings.Builder builder, MobCategory category, EntityType<?> type, int weight,
                         int minCount, int maxCount) {
        builder.addSpawn(type, category, weight, UniformInt.of(minCount, maxCount));
    }

    @Override
    public void addSpawnCost(MobSpawnSettings.Builder builder, EntityType<?> type, double mass, double gravity) {
        builder.addMobSpawnCost(type, mass, gravity);
    }

    @Override
    public boolean setCreatureGenerationProbability(MobSpawnSettings.Builder builder, float probability) {
        return false;
    }

    @Override
    public void copyVanillaSpawnSettings(Biome.BiomeBuilder builder, Biome biome) {
        // 26.3 stores natural spawn settings in the biome's environment attributes,
        // which NMSBiomeInjector already copies into its builder.
    }

    @Override
    public void setSpawnSettings(Biome.BiomeBuilder builder, MobSpawnSettings settings) {
        builder.setAttribute(EnvironmentAttributes.NATURAL_MOB_SPAWNS, settings);
    }

    @Override
    public void applyBiomeColorOverrides(Biome.BiomeBuilder builder, VanillaBiomeProperties properties) {
        if(properties.getFogColor() != null) {
            builder.modifyAttribute(EnvironmentAttributes.FOG_COLOR, AttributeModifier.override(), color(properties.getFogColor()));
        }
        if(properties.getWaterFogColor() != null) {
            builder.modifyAttribute(EnvironmentAttributes.WATER_FOG_COLOR, AttributeModifier.override(), color(properties.getWaterFogColor()));
        }
        if(properties.getSkyColor() != null) {
            builder.modifyAttribute(EnvironmentAttributes.SKY_COLOR, AttributeModifier.override(), color(properties.getSkyColor()));
        }
    }

    private static org.joml.Vector3f color(int rgb) {
        return new org.joml.Vector3f(
            ((rgb >>> 16) & 0xFF) / 255.0f,
            ((rgb >>> 8) & 0xFF) / 255.0f,
            (rgb & 0xFF) / 255.0f
        );
    }

    @Override
    public void applyStructureBeard(ChunkGenerator generator, StructureManager structures, ChunkAccess chunk, WorldProperties world,
                                    BiomeProvider biomeProvider, PreLoadCompatibilityOptions options) {
        Beardifier beardifier = Beardifier.forStructuresInChunk(structures, chunk.getPos());
        NMSStructureBeardifier.apply(generator, chunk, world, biomeProvider, options,
            (x, y, z) -> beardifier.sampleValue(SamplerContext.EMPTY_UNCACHED, x, y, z));
    }
}
