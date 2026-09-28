package com.dfsek.terra.bukkit.nms.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;


public class SoundEventTemplate implements ObjectTemplate<SoundEvent> {
    @Value("id")
    @Default
    private Identifier id = null;

    @Value("distance-to-travel")
    @Default
    private Float distanceToTravel = null;

    @Override
    public SoundEvent get() {
        if(id == null) {
            return null;
        } else if(distanceToTravel == null) {
            return SoundEvent.createVariableRangeEvent(id);
        } else {
            return SoundEvent.createFixedRangeEvent(id, distanceToTravel);
        }
    }
}
