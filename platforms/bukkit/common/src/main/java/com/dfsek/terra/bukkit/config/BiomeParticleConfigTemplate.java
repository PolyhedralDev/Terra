package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.mojang.brigadier.StringReader;
import dev.wyck.environment.particle.ParticleCatalog;
import dev.wyck.environment.particle.ParticleOptions;


public class BiomeParticleConfigTemplate implements ObjectTemplate<ParticleCatalog> {
    @Value("particle")
    @Default
    private String particle = null;

    @Value("probability")
    @Default
    private Float probability = 0.1f;

    @Override
    public ParticleCatalog get() {
        if(particle == null) {
            return null;
        } else {
            ParticleOptions particleOptions = ParticleArgument.readParticle(new StringReader(particle));
            return ParticleCatalog.builder()
                .particle(particleOptions) // TODO: Check that this supports custom particles
                .probability(probability)
                .build();
        }
    }
}
