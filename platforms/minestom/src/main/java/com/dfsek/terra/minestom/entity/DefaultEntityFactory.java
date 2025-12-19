package com.dfsek.terra.minestom.entity;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;

import com.dfsek.terra.minestom.api.EntityFactory;


public class DefaultEntityFactory implements EntityFactory {
    @Override
    public Entity createEntity(EntityType type) {
        return new Entity(type);
    }
}
