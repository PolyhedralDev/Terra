package com.dfsek.terra.addons.chunkgenerator.layer.sampler.chunk;

import com.dfsek.seismic.math.floatingpoint.FloatingPointFunctions;
import com.dfsek.seismic.math.numericanalysis.interpolation.InterpolationFunctions;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.api.chunk.ChunkLayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.DensityLayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.blend.BlendProperties;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;

public class DensityChunkLayerSampler implements ChunkLayerSampler {
    double[] samples;

    private final int blendExtent;

    private final int min;
    private final int blendExtentYExtent;

    private final int blendExtentMinus2;
    private final int blendYExtendMinus2;
    private final int blendDensity;

    public DensityChunkLayerSampler(int chunkX, int chunkZ, WorldProperties world, BiomeProvider biomeProvider, DensityLayerSampler layerSampler, BlendProperties blendProperties) {
        this.min = world.getMinHeight() - 1;
        int worldMax = world.getMaxHeight() + 1;

        blendDensity = blendProperties.density();

        int blendYRange = worldMax - min + 1;

        int max;
        int blendYExtend;
        if (blendYRange % blendDensity == 0) {
            blendYExtend = blendYRange / blendDensity;
            max = worldMax;
        } else {
            blendYExtend = (blendYRange / blendDensity) + 1;
            max = worldMax + 1;
        }

        blendExtent = blendProperties.extent();
        blendExtentYExtent = blendYExtend * blendExtent;
        blendExtentMinus2 = blendExtent - 2;
        blendYExtendMinus2 = blendYExtend - 2;

        samples = new double[blendExtentYExtent * blendExtent];

        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;

        for (int x = 0; x < 18; x += blendDensity) {
            for (int z = 0; z < 18; z += blendDensity) {
                int cx = xOrigin + x;
                int cz = zOrigin + z;
                for (int y = this.min; y <= max; y++) {
                    int yi = y - this.min;
                    int index = x * blendExtentYExtent + yi * blendExtent + z;
                    samples[index] = layerSampler.sample(cx, y, cz, world, biomeProvider);
                }
            }
        }
    }

    private double getSample(int x, int y, int z) {
        int index = x * blendExtentYExtent + y * blendExtent + z;
        return samples[index];
    }

    @Override
    public double sample(int fmX, int y, int fmZ) {
        double gx = (double)(fmX + 1) / blendDensity;
        double gz = (double)(fmZ + 1) / blendDensity;
        double gy = (double)(y - min) / blendDensity;

        int x0 = Math.max(0, Math.min(blendExtentMinus2, FloatingPointFunctions.floor(gx)));
        int z0 = Math.max(0, Math.min(blendExtentMinus2, FloatingPointFunctions.floor(gz)));
        int y0 = Math.max(0, Math.min(blendYExtendMinus2, FloatingPointFunctions.floor(gy)));

        int x1 = x0 + 1;
        int z1 = z0 + 1;
        int y1 = y0 + 1;

        double tx = gx - x0;
        double tz = gz - z0;
        double ty = gy - y0;

        // Fetch 8 corners
        double c000 = getSample(x0, y0, z0);
        double c100 = getSample(x1, y0, z0);
        double c010 = getSample(x0, y1, z0);
        double c110 = getSample(x1, y1, z0);
        double c001 = getSample(x0, y0, z1);
        double c101 = getSample(x1, y0, z1);
        double c011 = getSample(x0, y1, z1);
        double c111 = getSample(x1, y1, z1);

        return InterpolationFunctions.triLerp(c000, c100, c010, c110, c001, c101, c011, c111, tx, ty, tz);
    }
}