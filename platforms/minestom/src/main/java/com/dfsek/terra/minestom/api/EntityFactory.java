package com.dfsek.terra.minestom.api;


import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;


/**
 * Allows adding AI to generated entities using custom entity types
 */
public interface EntityFactory {
    Entity createEntity(EntityType type);
}
