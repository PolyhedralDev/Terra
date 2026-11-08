package com.dfsek.terra.addons.attribute.minecraft;

import com.dfsek.terra.api.registry.key.StringIdentifiable;


public enum CardinalLight implements StringIdentifiable {
    DEFAULT("default"),
    NETHER("nether");

    private final String id;

    CardinalLight(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }
}
