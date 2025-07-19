package com.dfsek.terra.bukkit.nms.v1_21_8.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;


public class BiomeAdditionsSoundTemplate implements ObjectTemplate<AmbientAdditionsSettings> {
    @Value("sound")
    @Default
    private SoundEvent sound = null;
    
    @Value("sound-chance")
    @Default
    private Double soundChance = null;
    
    @Override
    public AmbientAdditionsSettings get() {
        if(sound == null || soundChance == null) {
            return null;
        } else {
            return new AmbientAdditionsSettings(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), soundChance);
        }
    }
}
