package com.dfsek.terra.api.world.attribute.value;

import com.dfsek.terra.api.registry.key.StringIdentifiable;


public enum TriState implements StringIdentifiable {
    TRUE("true"),
    FALSE("false"),
    DEFAULT("default");

    private final String id;

    TriState(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }
}
