/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.entity;

import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.entity.EntityType;


public interface MobSpawner extends BlockEntity {
    EntityType getSpawnedType();
    
    void setSpawnedType(@NotNull EntityType creatureType);
    
    int getDelay();
    
    void setDelay(int delay);
    
    int getMinSpawnDelay();
    
    void setMinSpawnDelay(int delay);
    
    int getMaxSpawnDelay();
    
    void setMaxSpawnDelay(int delay);
    
    int getSpawnCount();
    
    void setSpawnCount(int spawnCount);
    
    int getMaxNearbyEntities();
    
    void setMaxNearbyEntities(int maxNearbyEntities);
    
    int getRequiredPlayerRange();
    
    void setRequiredPlayerRange(int requiredPlayerRange);
    
    int getSpawnRange();
    
    void setSpawnRange(int spawnRange);
}
