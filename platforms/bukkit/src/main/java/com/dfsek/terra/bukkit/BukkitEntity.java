package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

public class BukkitEntity implements Entity {
    private final org.bukkit.entity.Entity entity;

    public BukkitEntity(org.bukkit.entity.Entity entity) {
        this.entity = entity;
    }

    @Override
    public org.bukkit.entity.Entity getHandle() {
        return entity;
    }

    @Override
    public Location getLocation() {
        return BukkitAdapter.adapt(entity.getLocation());
    }

    @Override
    public void sendMessage(String message) {
        entity.sendMessage(message);
    }
}
