package com.dfsek.terra.addons.biome.pipeline.stage.expander;

import com.dfsek.terra.addons.biome.pipeline.api.Expander;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.pipeline.BiomeChunkImpl;
import com.dfsek.seismic.type.sampler.Sampler;


public class FractalExpander implements Expander {

    private final Sampler sampler;

    public FractalExpander(Sampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public PipelineBiome fillBiome(BiomeChunkImpl.ViewPoint viewPoint) {

        int xMod2 = viewPoint.gridX() % 2;
        int zMod2 = viewPoint.gridZ() % 2;

        double roll = sampler.getSample(viewPoint.worldSeed(), viewPoint.worldX(), viewPoint.worldZ());

        if(xMod2 == 1 && zMod2 == 0) { // Pick one of 2 neighbors on X axis randomly
            return roll > 0 ? viewPoint.getRelativeBiome(-1, 0) : viewPoint.getRelativeBiome(1, 0);

        } else if(xMod2 == 0 && zMod2 == 1) { // Pick one of 2 neighbors on Z axis randomly
            return roll > 0 ? viewPoint.getRelativeBiome(0, -1) : viewPoint.getRelativeBiome(0, 1);

        } else { // Pick one of 4 corners randomly
            return roll > 0 ?
                   roll > 0.25 ? viewPoint.getRelativeBiome(-1, 1) : viewPoint.getRelativeBiome(1, 1) :
                   roll > -0.25 ? viewPoint.getRelativeBiome(-1, -1) : viewPoint.getRelativeBiome(1, -1);
        }
    }
}
