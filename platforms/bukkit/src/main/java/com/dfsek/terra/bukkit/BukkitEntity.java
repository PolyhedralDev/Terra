package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.platform.world.entity.Entity;

public class BukkitEntity implements Entity {
    private final org.bukkit.entity.Entity entity;

    public BukkitEntity(org.bukkit.entity.Entity entity) {
        this.entity = entity;
    }

    @Override
    public org.bukkit.entity.Entity getHandle() {
        return entity;
    }
}
