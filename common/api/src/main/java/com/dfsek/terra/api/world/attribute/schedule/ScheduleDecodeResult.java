package com.dfsek.terra.api.world.attribute.schedule;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public record ScheduleDecodeResult(Optional<AttributeSchedule> schedule, List<Problem> droppedTracks, Optional<Problem> skipped) {
    public ScheduleDecodeResult {
        Objects.requireNonNull(schedule, "schedule");
        droppedTracks = List.copyOf(droppedTracks);
        Objects.requireNonNull(skipped, "skipped");
        if(schedule.isPresent() == skipped.isPresent()) {
            throw new IllegalArgumentException("exactly one of schedule and skipped must be present");
        }
    }


    public record Problem(String key, String reason) { }
}
