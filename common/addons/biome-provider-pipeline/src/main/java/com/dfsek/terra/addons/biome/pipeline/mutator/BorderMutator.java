package com.dfsek.terra.addons.biome.pipeline.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;


public class BorderMutator implements BiomeMutator {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<TerraBiome> replace;
    private final String replaceTag;
    
    public BorderMutator(String border, String replaceTag, NoiseSampler noiseSampler, ProbabilityCollection<TerraBiome> replace) {
        this.border = border;
        this.noiseSampler = noiseSampler;
        this.replace = replace;
        this.replaceTag = replaceTag;
    }
    
    @Override
    public TerraBiome mutate(ViewPoint viewPoint, double x, double z, long seed) {
        TerraBiome origin = viewPoint.getBiome(0, 0);
        if(origin.getTags().contains(replaceTag)) {
            for(int xi = -1; xi <= 1; xi++) {
                for(int zi = -1; zi <= 1; zi++) {
                    if(xi == 0 && zi == 0) continue;
                    TerraBiome current = viewPoint.getBiome(xi, zi);
                    if(current == null) continue;
                    if(current.getTags().contains(border)) {
                        TerraBiome biome = replace.get(noiseSampler, x, z, seed);
                        return biome == null ? origin : biome;
                    }
                }
            }
        }
        return origin;
    }
}
