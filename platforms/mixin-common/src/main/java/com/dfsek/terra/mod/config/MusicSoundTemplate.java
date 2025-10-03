package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.registry.Registries;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;

import com.dfsek.terra.api.util.range.Range;


public class MusicSoundTemplate implements ObjectTemplate<MusicSound> {
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
    public MusicSound get() {
        if(sound == null || delay == null || replaceCurrentMusic == null) {
            return null;
        } else {
            return new MusicSound(Registries.SOUND_EVENT.getEntry(sound), delay.getMin(), delay.getMax(), replaceCurrentMusic);
        }
    }
}
