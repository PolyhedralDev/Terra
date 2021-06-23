package com.dfsek.terra.bukkit.world.entity;

import com.dfsek.terra.api.entity.EntityType;

public class BukkitEntityType implements EntityType {
    private final org.bukkit.entity.EntityType delegate;

    public BukkitEntityType(org.bukkit.entity.EntityType delegate) {
        this.delegate = delegate;
    }

    @Override
    public org.bukkit.entity.EntityType getHandle() {
        return delegate;
    }
}
