package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.Position;

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
    public TerraBiome mutate(ViewPoint viewPoint, Position position) {
        TerraBiome origin = viewPoint.getBiome(0, 0);
        if(origin.getTags().contains(tag)) {
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    if(x == 0 && z == 0) continue;
                    TerraBiome current = viewPoint.getBiome(x, z);
                    if(current == null) continue;
                    if(borders.stream().anyMatch(current.getTags()::contains))
                        return replace.get(noiseSampler, position.getX(), position.getY());
                }
            }
        }
        return origin;
    }
}
