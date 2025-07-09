package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.particle.Particle;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeParticleConfig;


public class BiomeParticleConfigTemplate implements ObjectTemplate<BiomeParticleConfig> {
    @Value("particle")
    @Default
    private Identifier particle = null;

    @Value("probability")
    @Default
    private Integer probability = null;

    @Override
    public BiomeParticleConfig get() {
        if(particle == null || probability == null) {
            return null;
        }

        return new BiomeParticleConfig((ParticleEffect) Registries.PARTICLE_TYPE.get(particle),
            probability);
    }
}
