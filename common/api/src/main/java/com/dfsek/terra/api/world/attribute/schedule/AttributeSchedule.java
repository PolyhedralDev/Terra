package com.dfsek.terra.api.world.attribute.schedule;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;


/**
 * Both maps are unmodifiable copies in the given order.
 */
public record AttributeSchedule(RegistryKey clock, OptionalInt periodTicks, Map<Attribute<?>, ScheduleTrack<?>> tracks,
                                 Map<RegistryKey, TimeMarker> timeMarkers) {
    public AttributeSchedule {
        tracks = Collections.unmodifiableMap(new LinkedHashMap<>(tracks));
        timeMarkers = Collections.unmodifiableMap(new LinkedHashMap<>(timeMarkers));
    }
}
