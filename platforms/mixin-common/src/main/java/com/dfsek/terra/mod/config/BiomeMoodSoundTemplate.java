package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvent;


public class BiomeMoodSoundTemplate implements ObjectTemplate<BiomeMoodSound> {
    @Value("sound")
    @Default
    private SoundEvent sound = null;
    
    @Value("cultivation-ticks")
    @Default
    private Integer soundCultivationTicks = null;
    
    @Value("spawn-range")
    @Default
    private Integer soundSpawnRange = null;
    
    @Value("extra-distance")
    @Default
    private Double soundExtraDistance = null;
    
    @Override
    public BiomeMoodSound get() {
        if(sound == null || soundCultivationTicks == null || soundSpawnRange == null || soundExtraDistance == null) {
            return null;
        } else {
            return new BiomeMoodSound(sound, soundCultivationTicks, soundSpawnRange, soundExtraDistance);
        }
    }
}
