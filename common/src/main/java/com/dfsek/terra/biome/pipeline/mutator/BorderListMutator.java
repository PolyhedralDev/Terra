package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.biome.TerraBiome;

import java.util.Map;

public class BorderListMutator implements BiomeMutator {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<TerraBiome> replaceDefault;
    private final String defaultReplace;
    private final Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace;

    public BorderListMutator(Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace, String border, String defaultReplace, NoiseSampler noiseSampler, ProbabilityCollection<TerraBiome> replaceDefault) {
        this.border = border;
        this.noiseSampler = noiseSampler;
        this.replaceDefault = replaceDefault;
        this.defaultReplace = defaultReplace;
        this.replace = replace;
    }

    @Override
    public TerraBiome mutate(ViewPoint viewPoint, double x, double z) {
        TerraBiome origin = viewPoint.getBiome(0, 0);
        if(origin.getTags().contains(defaultReplace)) {
            for(int xi = -1; xi <= 1; xi++) {
                for(int zi = -1; zi <= 1; zi++) {
                    if(xi == 0 && zi == 0) continue;
                    TerraBiome current = viewPoint.getBiome(xi, zi);
                    if(current == null) continue;
                    if(current.getTags().contains(border)) {
                        if(replace.containsKey(origin)) {
                            TerraBiome biome = replace.get(origin).get(noiseSampler, x, z);
                            return biome == null ? origin : biome;
                        }
                        TerraBiome biome = replaceDefault.get(noiseSampler, x, z);
                        return biome == null ? origin : biome;
                    }
                }
            }
        }
        return origin;
    }
}
