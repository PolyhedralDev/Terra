/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events.world.generation;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.event.events.PackEvent;


/**
 * Called when an entity is spawned.
 */
public class EntitySpawnEvent implements PackEvent {
    private final ConfigPack pack;
    private final Entity entity;
    
    public EntitySpawnEvent(ConfigPack pack, Entity entity) {
        this.pack = pack;
        this.entity = entity;
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
}
