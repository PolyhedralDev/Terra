package com.dfsek.terra.minestom.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.world.attribute.AmbientSounds;
import net.minestom.server.world.biome.BiomeEffects;


public class BiomeMoodSoundTemplate implements ObjectTemplate<AmbientSounds.Mood> {
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
    public AmbientSounds.Mood get() {
        if(sound == null) return null;
        return new AmbientSounds.Mood(
            sound,
            soundCultivationTicks,
            soundSpawnRange,
            soundExtraDistance
        );
    }
}
