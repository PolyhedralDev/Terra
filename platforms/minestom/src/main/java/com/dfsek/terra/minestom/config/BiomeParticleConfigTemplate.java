package com.dfsek.terra.minestom.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minestom.server.particle.Particle;
import net.minestom.server.world.attribute.AmbientParticle;
import net.minestom.server.world.biome.BiomeEffects;
import org.slf4j.LoggerFactory;


public class BiomeParticleConfigTemplate implements ObjectTemplate<AmbientParticle> {
    @Value("particle")
    @Default
    private String particle = null;

    @Value("probability")
    @Default
    private Float probability = null;

    @Override
    public AmbientParticle get() {
        if(particle == null || probability == null) {
            return null;
        }

        String[] parts = particle.split("\\{");
        Particle parsedParticle = Particle.fromKey(parts[0]);
        if(parts.length > 1) {
            LoggerFactory.getLogger(BiomeParticleConfigTemplate.class).warn("Particle {} has additional data, particle will be ignored.",
                particle);
            return null;
        }

        return new AmbientParticle(
            parsedParticle,
            probability
        );
    }
}
