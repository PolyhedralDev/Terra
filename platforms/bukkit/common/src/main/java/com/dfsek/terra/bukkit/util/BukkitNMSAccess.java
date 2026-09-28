package com.dfsek.terra.bukkit.util;

import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;

import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.entity.EntityTypeExtended;


public interface BukkitNMSAccess {
    EntityType getEntityType(String data);

    org.bukkit.entity.Entity spawnEntity(Object target, Location location, EntityType type);

    void setSpawnerEntity(CreatureSpawner spawner, EntityTypeExtended type);
}
