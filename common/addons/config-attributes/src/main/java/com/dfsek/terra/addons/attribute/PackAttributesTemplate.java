package com.dfsek.terra.addons.attribute;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dfsek.terra.api.config.meta.Meta;


public class PackAttributesTemplate implements ConfigTemplate {
    @Value("attributes")
    @Default
    private @Meta Map<@Meta String, LazyConfigValue> attributes = Map.of();

    @Value("schedules")
    @Default
    private @Meta List<@Meta String> schedules = List.of();

    @Value("schedule-definitions")
    @Default
    private @Meta Map<@Meta String, LazyConfigValue> scheduleDefinitions = Map.of();

    // May be a map (vanilla.mobs); only a string names a parent.
    @Value("vanilla")
    @Default
    private @Meta LazyConfigValue vanilla = null;

    @Value("vanilla-generation")
    @Default
    private @Meta LazyConfigValue vanillaGeneration = null;

    Map<String, LazyConfigValue> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public List<String> getSchedules() {
        return schedules;
    }

    Map<String, LazyConfigValue> getScheduleDefinitions() {
        return Collections.unmodifiableMap(scheduleDefinitions);
    }

    public Optional<String> getVanilla() {
        return string(vanilla);
    }

    public Optional<String> getVanillaGeneration() {
        return string(vanillaGeneration);
    }

    private static Optional<String> string(LazyConfigValue value) {
        return value != null && value.raw() instanceof String s ? Optional.of(s) : Optional.empty();
    }
}
