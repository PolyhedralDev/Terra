package com.dfsek.terra.population.items.carving;

import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.platform.world.ChunkAccess;
import com.dfsek.terra.carving.Carver;
import com.dfsek.terra.generation.math.interpolation.NoiseChunkInterpolator;

public class NoiseCarver implements Carver {
    private final NoiseSampler noise;
    private final Range range;

    public NoiseCarver(NoiseSampler noise, Range range) {
        this.noise = noise;
        this.range = range;
    }

    @Override
    public void carve(int chunkX, int chunkZ, ChunkAccess chunk) {
        NoiseChunkInterpolator chunkInterpolator = new NoiseChunkInterpolator(chunkX, chunkZ, noise);

        for(int y : range) {

        }
    }
}
