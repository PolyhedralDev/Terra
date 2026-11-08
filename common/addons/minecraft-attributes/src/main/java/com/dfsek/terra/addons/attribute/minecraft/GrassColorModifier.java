package com.dfsek.terra.addons.attribute.minecraft;

import com.dfsek.terra.api.registry.key.StringIdentifiable;


public enum GrassColorModifier implements StringIdentifiable {
    NONE("none"),
    DARK_FOREST("dark_forest"),
    SWAMP("swamp");

    private final String id;

    GrassColorModifier(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }
}
