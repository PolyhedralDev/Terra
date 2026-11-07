package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import dev.wyck.environment.sounds.SoundEvent;
import dev.wyck.keys.ResourceKey;


public class SoundEventTemplate implements ObjectTemplate<SoundEvent> {
    @Value("id")
    @Default
    private ResourceKey id = null;

    @Value("distance-to-travel")
    @Default
    private Float distanceToTravel = null;

    @Override
    public SoundEvent get() {
        if(id == null) {
            return null;
        } else if(distanceToTravel == null) {
            return SoundEvent.variableRange(id);
        } else {
            return SoundEvent.fixedRange(id, distanceToTravel);
        }
    }
}
