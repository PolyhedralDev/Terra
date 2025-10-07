package com.dfsek.terra.minestom.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentParticle;
import net.minestom.server.particle.Particle;
import net.minestom.server.world.biome.BiomeEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


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

        String[] parts = particle.split("\\{");
        Particle parsedParticle = Particle.fromKey(parts[0]);
        if (parts.length > 1) {
            LoggerFactory.getLogger(BiomeParticleConfigTemplate.class).warn("Particle {} has additional data, which will be ignored.", particle);
        }

        return new BiomeEffects.Particle(
            probability,
            parsedParticle
        );
    }
}
