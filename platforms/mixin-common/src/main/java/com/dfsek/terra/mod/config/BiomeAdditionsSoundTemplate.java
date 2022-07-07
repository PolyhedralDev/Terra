package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.SoundEvent;


public class BiomeAdditionsSoundTemplate implements ObjectTemplate<BiomeAdditionsSound> {
    @Value("sound")
    @Default
    private SoundEvent sound = null;
    
    @Value("sound-chance")
    @Default
    private Double soundChance = null;
    
    @Override
    public BiomeAdditionsSound get() {
        if(sound == null || soundChance == null) {
            return null;
        } else {
            return new BiomeAdditionsSound(sound, soundChance);
        }
    }
}
