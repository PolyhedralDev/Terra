package com.dfsek.terra.api.event.events.world.generation;

import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedEntity;
import com.dfsek.terra.config.pack.ConfigPack;

/**
 * Called when an entity is spawned via {@link BufferedEntity}.
 */
public class EntitySpawnEvent implements PackEvent {
    private final ConfigPack pack;
    private final Entity entity;
    private final Location location;

    public EntitySpawnEvent(ConfigPack pack, Entity entity, Location location) {
        this.pack = pack;
        this.entity = entity;
        this.location = location;
    }

    @Override
    public ConfigPack getPack() {
        return pack;
    }

    /**
     * Get the entity that triggered the event.
     *
     * @return The entity.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Get the location of the entity.
     *
     * @return Location of the entity.
     */
    public Location getLocation() {
        return location;
    }
}
