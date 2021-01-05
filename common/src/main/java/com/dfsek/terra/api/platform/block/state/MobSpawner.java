package com.dfsek.terra.api.platform.block.state;

import com.dfsek.terra.api.platform.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public interface MobSpawner extends BlockState {
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
