package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import dev.wyck.environment.sounds.Music;
import dev.wyck.environment.sounds.SoundEvent;


public class MusicSoundTemplate implements ObjectTemplate<Music> {
    @Value("sound")
    @Default
    private SoundEvent sound = null;

    @Value("min-delay")
    @Default
    private Integer minDelay = null;

    @Value("max-delay")
    @Default
    private Integer maxDelay = null;

    @Value("replace-current-music")
    @Default
    private Boolean replaceCurrentMusic = null;

    @Override
    public Music get() {
        if(sound == null || minDelay == null || maxDelay == null || replaceCurrentMusic == null) {
            return null;
        } else {
            return Music.builder()
                .sound(sound)
                .minDelay(minDelay)
                .maxDelay(maxDelay)
                .replaceCurrentMusic(replaceCurrentMusic)
                .build();
        }
    }
}
