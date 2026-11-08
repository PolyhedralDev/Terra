package com.dfsek.terra.addons.attribute.minecraft;

import com.dfsek.terra.api.registry.key.StringIdentifiable;


public enum MoonPhase implements StringIdentifiable {
    FULL_MOON("full_moon"),
    WANING_GIBBOUS("waning_gibbous"),
    THIRD_QUARTER("third_quarter"),
    WANING_CRESCENT("waning_crescent"),
    NEW_MOON("new_moon"),
    WAXING_CRESCENT("waxing_crescent"),
    FIRST_QUARTER("first_quarter"),
    WAXING_GIBBOUS("waxing_gibbous");

    private final String id;

    MoonPhase(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }
}
