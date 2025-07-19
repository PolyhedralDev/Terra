package com.dfsek.terra.bukkit.nms.v1_21_8.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientMoodSettings;


public class BiomeMoodSoundTemplate implements ObjectTemplate<AmbientMoodSettings> {
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
    public AmbientMoodSettings get() {
        if(sound == null || soundCultivationTicks == null || soundSpawnRange == null || soundExtraDistance == null) {
            return null;
        } else {
            return new AmbientMoodSettings(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), soundCultivationTicks, soundSpawnRange, soundExtraDistance);
        }
    }
}
