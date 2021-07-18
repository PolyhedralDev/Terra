package com.dfsek.terra.addons.biome.pipeline.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.Objects;

public class SmoothMutator implements BiomeMutator {

    private final NoiseSampler sampler;

    public SmoothMutator(NoiseSampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public TerraBiome mutate(ViewPoint viewPoint, double x, double z) {
        TerraBiome top = viewPoint.getBiome(1, 0);
        TerraBiome bottom = viewPoint.getBiome(-1, 0);
        TerraBiome left = viewPoint.getBiome(0, 1);
        TerraBiome right = viewPoint.getBiome(0, -1);


        boolean vert = Objects.equals(top, bottom) && top != null;
        boolean horiz = Objects.equals(left, right) && left != null;

        if(vert && horiz) {
            return MathUtil.normalizeIndex(sampler.getNoise(x, z), 2) == 0 ? left : top;
        }

        if(vert) return top;
        if(horiz) return left;

        return viewPoint.getBiome(0, 0);
    }
}
