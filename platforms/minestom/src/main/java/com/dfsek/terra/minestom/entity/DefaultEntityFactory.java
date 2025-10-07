package com.dfsek.terra.minestom.entity;

import com.dfsek.terra.minestom.api.EntityFactory;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;


public class DefaultEntityFactory implements EntityFactory {
    @Override
    public Entity createEntity(EntityType type) {
        return new Entity(type);
    }
}
