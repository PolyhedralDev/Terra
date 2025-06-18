package com.dfsek.terra.minestom.entity;


import net.minestom.server.entity.EntityType;


public class MinestomEntityType implements com.dfsek.terra.api.entity.EntityType {
    private final EntityType delegate;

    public MinestomEntityType(String id) {
        delegate = EntityType.fromKey(id);
    }

    @Override
    public EntityType getHandle() {
        return delegate;
    }
}
