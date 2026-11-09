package com.dfsek.terra.world.attribute.platform;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AttributeReportTest {
    private final List<String> warnings = new ArrayList<>();
    private final List<String> debugs = new ArrayList<>();
    private final AttributeReport report = new AttributeReport("test", warnings::add, debugs::add);

    @Test
    void reportFormats() {
        report.attributeSkipped("BIOME", "minecraft:visual/fog_color", "not a colour");
        report.scheduleSkipped("mypack:seasons", "no clock");
        report.trackDropped("mypack:seasons", "minecraft:visual/fog_color", "bad keyframe");
        report.timeMarkerDropped("mypack:seasons", "minecraft:noon", "minecraft:overworld");
        report.ignored("pack.yml", "vanilla", "no such dimension type \"minecraft:nope\"");
        report.tagCreated("BIOME", "minecraft:is_custom");
        report.notApplied("test:custom");
        report.notApplied("test:custom");
        report.notApplied("test:other");

        assertEquals(List.of(
            "Pack \"test\", config \"BIOME\": attribute \"minecraft:visual/fog_color\" skipped: not a colour",
            "Pack \"test\": schedule \"mypack:seasons\" skipped: no clock",
            "Pack \"test\": schedule \"mypack:seasons\" track \"minecraft:visual/fog_color\" dropped: bad keyframe",
            "Pack \"test\": schedule \"mypack:seasons\" time marker \"minecraft:noon\" dropped: already defined for clock "
            + "\"minecraft:overworld\"",
            "Pack \"test\", config \"pack.yml\": \"vanilla\" ignored: no such dimension type \"minecraft:nope\"",
            "Pack \"test\", config \"BIOME\": tag \"minecraft:is_custom\" created: no such tag in the minecraft namespace"), warnings);
        assertEquals(List.of(
            "Pack \"test\": attribute \"test:custom\" is not applied on this platform",
            "Pack \"test\": attribute \"test:other\" is not applied on this platform"), debugs);
    }

    @Test
    void notAppliedToWorldOnce() {
        report.notAppliedToWorld("terra:dimension/min_y", "world");
        report.notAppliedToWorld("terra:dimension/min_y", "world");

        assertEquals(List.of(), warnings);
        assertEquals(List.of(
            "Pack \"test\": attribute \"terra:dimension/min_y\" is not applied to world \"world\": it is fixed when the world is created"),
            debugs);
    }
}
