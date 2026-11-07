package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.wyck.environment.particle.AmbientParticle;
import dev.wyck.environment.particle.ParticleCatalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import com.dfsek.terra.bukkit.util.VanillaParticles;


public class BiomeParticleConfigTemplate implements ObjectTemplate<ParticleCatalog> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiomeParticleConfigTemplate.class);

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
        }

        try {
            AmbientParticle parsed = VanillaParticles.parse(particle, probability);
            return parsed == null ? null : new ParticleCatalog(List.of(parsed));
        } catch(CommandSyntaxException | RuntimeException e) {
            LOGGER.warn("Skipping particle \"{}\": {}", particle, e.getMessage());
            return null;
        }
    }
}
