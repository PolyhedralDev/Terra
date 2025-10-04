package com.dfsek.terra.minestom.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.world.biome.BiomeEffects;


public class BiomeAdditionsSoundTemplate implements ObjectTemplate<BiomeEffects.AdditionsSound> {
    @Value("sound")
    @Default
    private SoundEvent sound = null;

    @Value("sound-chance")
    @Default
    private Double soundChance = null;

    @Override
    public BiomeEffects.AdditionsSound get() {
        if(sound == null) return null;
        return new BiomeEffects.AdditionsSound(sound, soundChance);
    }
}
