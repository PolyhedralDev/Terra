package com.dfsek.terra.minestom.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.kyori.adventure.key.Key;
import net.minestom.server.sound.SoundEvent;


public class SoundEventTemplate implements ObjectTemplate<SoundEvent> {
    @Value("id")
    @Default
    private Key id = null;

    @Value("distance-to-travel")
    @Default
    private Float distanceToTravel = null;

    @Override
    public SoundEvent get() {
        if(id == null) {
            return null;
        } else {
            // distanceToTravel is specifically allowed to be null.
            return SoundEvent.of(id, distanceToTravel);
        }
    }
}
