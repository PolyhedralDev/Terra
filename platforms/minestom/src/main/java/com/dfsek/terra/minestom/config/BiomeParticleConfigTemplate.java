package com.dfsek.terra.minestom.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minestom.server.particle.Particle;
import net.minestom.server.world.biome.BiomeEffects;


public class BiomeParticleConfigTemplate implements ObjectTemplate<BiomeEffects.Particle> {
    @Value("particle")
    @Default
    private String particle = null;

    @Value("probability")
    @Default
    private Float probability = null;

    @Override
    public BiomeEffects.Particle get() {
        if(particle == null || probability == null) {
            return null;
        }

        return new BiomeEffects.Particle(
            probability,
            Particle.fromKey(particle)
        );
    }
}
