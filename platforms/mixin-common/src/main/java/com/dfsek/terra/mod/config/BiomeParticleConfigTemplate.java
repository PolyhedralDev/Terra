package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.world.biome.BiomeParticleConfig;


public class BiomeParticleConfigTemplate implements ObjectTemplate<BiomeParticleConfig> {
    @Value("particle")
    @Default
    private String particle = null;
    
    @Value("probability")
    @Default
    private Integer probability = null;
    
    @Override
    public BiomeParticleConfig get() {
        if(particle == null || probability == null) {
            return null;
        }
        
        try {
            return new BiomeParticleConfig(ParticleEffectArgumentType.readParameters(new StringReader(particle)), probability);
        } catch(CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
