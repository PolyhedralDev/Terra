package com.dfsek.terra.addons.attribute.minecraft;

import com.dfsek.terra.api.registry.key.StringIdentifiable;


public enum Skybox implements StringIdentifiable {
    NONE("none"),
    OVERWORLD("overworld"),
    END("end");

    private final String id;

    Skybox(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }
}
