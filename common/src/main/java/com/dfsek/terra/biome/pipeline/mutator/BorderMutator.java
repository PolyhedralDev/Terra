package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.Set;

public class BorderMutator implements BiomeMutator {
    private final Set<String> borders;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<TerraBiome> replace;
    private final String tag;

    public BorderMutator(Set<String> borders, String tag, NoiseSampler noiseSampler, ProbabilityCollection<TerraBiome> replace) {
        this.borders = borders;
        this.noiseSampler = noiseSampler;
        this.replace = replace;
        this.tag = tag;
    }

    @Override
    public TerraBiome mutate(ViewPoint viewPoint, double x, double z) {
        TerraBiome origin = viewPoint.getBiome(0, 0);
        if(origin.getTags().contains(tag)) {
            for(int xi = -1; xi <= 1; xi++) {
                for(int zi = -1; zi <= 1; zi++) {
                    if(xi == 0 && zi == 0) continue;
                    TerraBiome current = viewPoint.getBiome(xi, zi);
                    if(current == null) continue;
                    if(borders.stream().anyMatch(current.getTags()::contains))
                        return replace.get(noiseSampler, x, z);
                }
            }
        }
        return origin;
    }
}
