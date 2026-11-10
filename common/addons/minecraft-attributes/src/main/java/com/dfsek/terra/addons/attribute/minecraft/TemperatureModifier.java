package com.dfsek.terra.addons.attribute.minecraft;

import com.dfsek.terra.api.registry.key.StringIdentifiable;


public enum TemperatureModifier implements StringIdentifiable {
    NONE("none"),
    FROZEN("frozen");

    private final String id;

    TemperatureModifier(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }
}
