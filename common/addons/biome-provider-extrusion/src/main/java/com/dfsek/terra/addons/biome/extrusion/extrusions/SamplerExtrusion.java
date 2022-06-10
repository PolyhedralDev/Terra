package com.dfsek.terra.addons.biome.extrusion.extrusions;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.Biome;

import java.security.cert.Extension;


/**
 * Extrusion which operates using a noise sampler
 */
public abstract class SamplerExtrusion implements Extrusion {
    protected NoiseSampler sampler;
    
    @Override
    public Biome extrude(Biome original, int x, int y, int z, long seed) {
        return null;
    }
}
