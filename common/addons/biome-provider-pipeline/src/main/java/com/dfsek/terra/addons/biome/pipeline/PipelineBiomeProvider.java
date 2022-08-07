package com.dfsek.terra.addons.biome.pipeline;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import net.jafama.FastMath;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeChunk;
import com.dfsek.terra.addons.biome.pipeline.api.Pipeline;
import com.dfsek.terra.addons.biome.pipeline.api.SeededVector;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class PipelineBiomeProvider implements BiomeProvider {
    
    private final LoadingCache<SeededVector, BiomeChunk> biomeChunkCache;
    private final int chunkSize;
    private final int resolution;
    private final NoiseSampler mutator;
    private final double noiseAmp;
    private final Set<Biome> biomes;
    
    public PipelineBiomeProvider(Pipeline pipeline, int resolution, NoiseSampler mutator, double noiseAmp) {
        this.resolution = resolution;
        this.mutator = mutator;
        this.noiseAmp = noiseAmp;
        this.chunkSize = pipeline.getChunkSize();
        this.biomeChunkCache = Caffeine.newBuilder()
                                       .maximumSize(1024)
                                       .build(pipeline::generateChunk);
    
        Set<PipelineBiome> biomeSet = new HashSet<>();
        pipeline.getSource().getBiomes().forEach(biomeSet::add);
        Iterable<PipelineBiome> result = biomeSet;
        for(Stage stage : pipeline.getStages()) {
            result = stage.getBiomes(result);
        }
        this.biomes = new HashSet<>();
        Iterable<PipelineBiome> finalResult = result;
        result.forEach(pipelineBiome -> {
            if(pipelineBiome.isPlaceholder()) {
            
                StringBuilder biomeList = new StringBuilder("\n");
                StreamSupport.stream(finalResult.spliterator(), false)
                             .sorted(Comparator.comparing(StringIdentifiable::getID))
                             .forEach(delegate -> biomeList
                                     .append("    - ")
                                     .append(delegate.getID())
                                     .append(':')
                                     .append(delegate.getClass().getCanonicalName())
                                     .append('\n'));
                throw new IllegalArgumentException("Biome Pipeline leaks placeholder biome \"" + pipelineBiome.getID() +
                                                   "\". Ensure there is a stage to guarantee replacement of the placeholder biome. Biomes: " +
                                                   biomeList);
            }
            this.biomes.add(pipelineBiome.getBiome());
        });
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        return getBiome(x, z, seed);
    }
    
    public Biome getBiome(int x, int z, long seed) {
    
        x += mutator.noise(seed + 1, x, z) * noiseAmp;
        z += mutator.noise(seed + 2, x, z) * noiseAmp;
        
        x /= resolution;
        z /= resolution;
        
        int chunkX = FastMath.floorDiv(x, chunkSize);
        int chunkZ = FastMath.floorDiv(z, chunkSize);
        
        int chunkWorldX = chunkX * chunkSize;
        int chunkWorldZ = chunkZ * chunkSize;
        
        int xInChunk = x - chunkWorldX;
        int zInChunk = z - chunkWorldZ;
        
        return biomeChunkCache.get(new SeededVector(seed, chunkWorldX, chunkWorldZ)).get(xInChunk, zInChunk).getBiome();
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return biomes;
    }
    
    @Override
    public Column<Biome> getColumn(int x, int z, long seed, int min, int max) {
        return new BiomePipelineColumn(this, min, max, x, z, seed);
    }
    
    @Override
    public int resolution() {
        return resolution;
    }
}
