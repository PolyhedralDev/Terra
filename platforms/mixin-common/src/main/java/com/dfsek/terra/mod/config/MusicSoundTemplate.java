package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;


public class MusicSoundTemplate implements ObjectTemplate<MusicSound> {
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
    public MusicSound get() {
        if(sound == null || minDelay == null || maxDelay == null || replaceCurrentMusic == null) {
            return null;
        } else {
            return new MusicSound(sound, minDelay, maxDelay, replaceCurrentMusic);
        }
    }
}
