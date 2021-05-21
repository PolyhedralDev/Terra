package com.dfsek.terra.bukkit.world.block.state;

import com.dfsek.terra.api.platform.block.state.MobSpawner;
import com.dfsek.terra.api.platform.block.state.SerialState;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;
import org.bukkit.block.CreatureSpawner;
import org.jetbrains.annotations.NotNull;

public class BukkitMobSpawner extends BukkitBlockState implements MobSpawner {
    protected BukkitMobSpawner(CreatureSpawner block) {
        super(block);
    }

    @Override
    public EntityType getSpawnedType() {
        return new BukkitEntityType(((CreatureSpawner) getHandle()).getSpawnedType());
    }

    @Override
    public void setSpawnedType(@NotNull EntityType creatureType) {
        ((CreatureSpawner) getHandle()).setSpawnedType(((BukkitEntityType) creatureType).getHandle());
    }

    @Override
    public int getDelay() {
        return ((CreatureSpawner) getHandle()).getDelay();
    }

    @Override
    public void setDelay(int delay) {
        ((CreatureSpawner) getHandle()).setDelay(delay);
    }

    @Override
    public int getMinSpawnDelay() {
        return ((CreatureSpawner) getHandle()).getMinSpawnDelay();
    }

    @Override
    public void setMinSpawnDelay(int delay) {
        ((CreatureSpawner) getHandle()).setMinSpawnDelay(delay);
    }

    @Override
    public int getMaxSpawnDelay() {
        return ((CreatureSpawner) getHandle()).getMaxSpawnDelay();
    }

    @Override
    public void setMaxSpawnDelay(int delay) {
        ((CreatureSpawner) getHandle()).setMaxSpawnDelay(delay);
    }

    @Override
    public int getSpawnCount() {
        return ((CreatureSpawner) getHandle()).getSpawnCount();
    }

    @Override
    public void setSpawnCount(int spawnCount) {
        ((CreatureSpawner) getHandle()).setSpawnCount(spawnCount);
    }

    @Override
    public int getMaxNearbyEntities() {
        return ((CreatureSpawner) getHandle()).getMaxNearbyEntities();
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {
        ((CreatureSpawner) getHandle()).setMaxNearbyEntities(maxNearbyEntities);
    }

    @Override
    public int getRequiredPlayerRange() {
        return ((CreatureSpawner) getHandle()).getRequiredPlayerRange();
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        ((CreatureSpawner) getHandle()).setRequiredPlayerRange(requiredPlayerRange);
    }

    @Override
    public int getSpawnRange() {
        return ((CreatureSpawner) getHandle()).getSpawnRange();
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        ((CreatureSpawner) getHandle()).setSpawnRange(spawnRange);
    }

    @Override
    public void applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            switch(k) {
                case "type":
                    setSpawnedType(new BukkitEntityType(org.bukkit.entity.EntityType.valueOf(v.toUpperCase())));
                    return;
                case "delay":
                    setDelay(Integer.parseInt(v));
                    return;
                case "min_delay":
                    setMinSpawnDelay(Integer.parseInt(v));
                    return;
                case "max_delay":
                    setMaxSpawnDelay(Integer.parseInt(v));
                    return;
                case "spawn_count":
                    setSpawnCount(Integer.parseInt(v));
                    return;
                case "spawn_range":
                    setSpawnRange(Integer.parseInt(v));
                    return;
                case "max_nearby":
                    setMaxNearbyEntities(Integer.parseInt(v));
                    return;
                case "required_player_range":
                    setRequiredPlayerRange(Integer.parseInt(v));
                    return;
                default:
                    throw new IllegalArgumentException("Invalid property: " + k);
            }
        });
    }
}
