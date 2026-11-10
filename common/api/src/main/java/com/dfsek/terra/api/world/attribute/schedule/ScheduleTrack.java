package com.dfsek.terra.api.world.attribute.schedule;

import java.util.List;

import com.dfsek.terra.api.world.attribute.Operation;


public record ScheduleTrack<T>(Operation operation, List<Keyframe> keyframes, Easing ease) {
    public ScheduleTrack {
        keyframes = List.copyOf(keyframes);
    }
}
