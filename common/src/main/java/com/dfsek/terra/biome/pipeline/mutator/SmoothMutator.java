package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.biome.pipeline.Position;

import java.util.Objects;

public class SmoothMutator implements BiomeMutator {

    private final NoiseSampler sampler;

    public SmoothMutator(NoiseSampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public Biome mutate(ViewPoint viewPoint, Position position) {
        Biome top = viewPoint.getBiome(1, 0);
        Biome bottom = viewPoint.getBiome(-1, 0);
        Biome left = viewPoint.getBiome(0, 1);
        Biome right = viewPoint.getBiome(0, -1);


        boolean vert = Objects.equals(top, bottom) && top != null;
        boolean horiz = Objects.equals(left, right) && left != null;

        if(vert && horiz) {
            return MathUtil.normalizeIndex(sampler.getNoise(position.getX(), position.getY()), 2) == 0 ? left : top;
        }

        if(vert) return top;
        if(horiz) return left;

        return viewPoint.getBiome(0, 0);
    }
}
