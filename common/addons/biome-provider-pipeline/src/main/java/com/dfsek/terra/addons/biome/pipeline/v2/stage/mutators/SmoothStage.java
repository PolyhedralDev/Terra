/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.v2.stage.mutators;

import java.util.Objects;

import com.dfsek.terra.addons.biome.pipeline.v2.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.v2.pipeline.BiomeChunkImpl;
import com.dfsek.terra.api.noise.NoiseSampler;


public class SmoothStage implements Stage {

    private final NoiseSampler sampler;

    public SmoothStage(NoiseSampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public PipelineBiome apply(BiomeChunkImpl.ViewPoint viewPoint) {
        PipelineBiome top = viewPoint.getRelativeBiome(1, 0);
        PipelineBiome bottom = viewPoint.getRelativeBiome(-1, 0);
        PipelineBiome left = viewPoint.getRelativeBiome(0, 1);
        PipelineBiome right = viewPoint.getRelativeBiome(0, -1);

        double roll = sampler.noise(viewPoint.worldSeed(), viewPoint.worldX(), viewPoint.worldZ());

        boolean vert = Objects.equals(top, bottom);
        boolean horiz = Objects.equals(left, right);

        if(vert && horiz) {
            return roll > 0 ?
                   roll > 0.25 ? left : right :
                   roll > -0.25 ? top : bottom;
        }
        if(vert) {
            return roll > 0 ? top : bottom;
        }
        if(horiz) {
            return roll > 0 ? left : right;
        }
        return viewPoint.getBiome();
    }

    @Override
    public int maxRelativeReadDistance() {
        return 1;
    }
}
