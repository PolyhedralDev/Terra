/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeHolder;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.Stage;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class BiomePipelineProvider implements BiomeProvider {
    private final LoadingCache<SeededVector, BiomeHolder> holderCache;
    private final BiomePipeline pipeline;
    private final int resolution;
    private final NoiseSampler mutator;
    private final double noiseAmp;
    
    private final Set<Biome> biomes;
    
    public BiomePipelineProvider(BiomePipeline pipeline, int resolution, NoiseSampler mutator, double noiseAmp) {
        this.resolution = resolution;
        this.mutator = mutator;
        this.noiseAmp = noiseAmp;
        holderCache = CacheBuilder.newBuilder()
                                  .maximumSize(1024)
                                  .build(
                                          new CacheLoader<>() {
                                              @Override
                                              public BiomeHolder load(@NotNull SeededVector key) {
                                                  return pipeline.getBiomes(key.x, key.z, key.seed);
                                              }
                                          }
                                        );
        this.pipeline = pipeline;
        
        Set<BiomeDelegate> biomeSet = new HashSet<>();
        pipeline.getSource().getBiomes().forEach(biomeSet::add);
        Iterable<BiomeDelegate> result = biomeSet;
        for(Stage stage : pipeline.getStages()) {
            result = stage.getBiomes(result); // pass through all stages
        }
        this.biomes = new HashSet<>();
        Iterable<BiomeDelegate> finalResult = result;
        result.forEach(biomeDelegate -> {
            if(biomeDelegate.isEphemeral()) {
                
                StringBuilder biomeList = new StringBuilder("\n");
                StreamSupport.stream(finalResult.spliterator(), false)
                             .sorted(Comparator.comparing(StringIdentifiable::getID))
                             .forEach(delegate -> biomeList
                                     .append("    - ")
                                     .append(delegate.getID())
                                     .append(':')
                                     .append(delegate.getClass().getCanonicalName())
                                     .append('\n'));
                throw new IllegalArgumentException("Biome Pipeline leaks ephemeral biome \"" + biomeDelegate.getID() +
                                                   "\". Ensure there is a stage to guarantee replacement of the ephemeral biome. Biomes: " +
                                                   biomeList);
            }
            this.biomes.add(biomeDelegate.getBiome());
        });
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        x += mutator.noise(seed + 1, x, z) * noiseAmp;
        z += mutator.noise(seed + 2, x, z) * noiseAmp;
        
        
        x = FastMath.floorToInt(FastMath.floorDiv(x, resolution));
        
        z = FastMath.floorToInt(FastMath.floorDiv(z, resolution));
        
        int fdX = FastMath.floorDiv(x, pipeline.getSize());
        int fdZ = FastMath.floorDiv(z, pipeline.getSize());
        return holderCache.getUnchecked(new SeededVector(fdX, fdZ, seed)).getBiome(x - fdX * pipeline.getSize(),
                                                                                   z - fdZ * pipeline.getSize()).getBiome();
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return biomes;
    }
    
    private record SeededVector(int x, int z, long seed) {
    }
}
