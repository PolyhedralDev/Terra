package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.attribute.AmbientParticle;
import java.util.stream.Stream;


public class BiomeParticleConfigTemplate implements ObjectTemplate<AmbientParticle> {
    @Value("particle")
    @Default
    private String particle = null;

    @Value("probability")
    @Default
    private Float probability = 0.1f;

    @Override
    public AmbientParticle get() {
        if(particle == null) {
            return null;
        }

        try {
            return new AmbientParticle(
                ParticleEffectArgumentType.readParameters(new StringReader(particle),
                    RegistryWrapper.WrapperLookup.of(Stream.of(Registries.PARTICLE_TYPE))),
                probability);
        } catch(CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
