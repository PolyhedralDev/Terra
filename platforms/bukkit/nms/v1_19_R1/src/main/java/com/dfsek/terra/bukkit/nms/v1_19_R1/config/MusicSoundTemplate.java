package com.dfsek.terra.bukkit.nms.v1_19_R1.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;

import com.dfsek.terra.api.util.Range;


public class MusicSoundTemplate implements ObjectTemplate<Music> {
    @Value("sound")
    @Default
    private SoundEvent sound = null;
    
    @Value("delay")
    @Default
    private Range delay = null;
    
    
    @Value("replace-current-music")
    @Default
    private Boolean replaceCurrentMusic = null;
    
    @Override
    public Music get() {
        if(sound == null || delay == null || replaceCurrentMusic == null) {
            return null;
        } else {
            return new Music(sound, delay.getMin(), delay.getMax(), replaceCurrentMusic);
        }
    }
}
