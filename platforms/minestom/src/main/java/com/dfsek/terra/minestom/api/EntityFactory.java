package com.dfsek.terra.minestom.api;


import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;


/**
 * Allows adding AI to generated entities using custom entity types
 */
public interface EntityFactory {
    /**
     * Creates a new entity of the specified type.
     *
     * @param type the type of the entity to be created
     * @return the created entity instance
     */
    Entity createEntity(EntityType type);

    /**
     * Creates a new entity of the specified type with additional data.
     *
     * @param type the type of the entity to be created
     * @param data the additional data for the entity, represented as a CompoundBinaryTag
     * @return the created entity instance
     */
    default Entity createEntity(EntityType type, CompoundBinaryTag data) {
        return createEntity(type);
    }
}
