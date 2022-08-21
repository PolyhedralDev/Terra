package com.dfsek.terra.addons.feature.distributor.distributors;

import com.dfsek.terra.api.noise.NoiseSampler;

import net.jafama.FastMath;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.util.MathUtil;


public class PaddedGridSamplerDistributor implements Distributor {
    private final NoiseSampler sampler;
    private final int width;
    
    private final int cellWidth;
    
    public PaddedGridSamplerDistributor(NoiseSampler sampler, int width, int padding) {
        this.sampler = sampler;
        this.width = width;
        this.cellWidth = width + padding;
    }
    
    @Override
    public boolean matches(int x, int z, long seed) {
        int cellX = FastMath.floorDiv(x, cellWidth);
        int cellZ = FastMath.floorDiv(z, cellWidth);
        
        int pointX = (int) (FastMath.round(MathUtil.lerp(MathUtil.inverseLerp(sampler.noise(x, z, seed), -1, 1), 0, width)) + cellX * cellWidth);
        int pointZ = (int) (FastMath.round(MathUtil.lerp(MathUtil.inverseLerp(sampler.noise(x, z, seed + 1), -1, 1), 0, width)) + cellZ * cellWidth);
        
        return x == pointX && z == pointZ;
    }
}
