package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import dev.wyck.environment.sounds.AmbientAdditionsSettings;
import dev.wyck.environment.sounds.SoundEvent;


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
            return AmbientAdditionsSettings.builder()
                .soundEvent(sound)
                .tickChance(soundChance)
                .build();
        }
    }
}
