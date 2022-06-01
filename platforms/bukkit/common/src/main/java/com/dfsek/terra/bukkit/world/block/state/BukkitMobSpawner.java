/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.bukkit.world.block.state;

import org.bukkit.block.CreatureSpawner;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.SerialState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;


public class BukkitMobSpawner extends BukkitBlockEntity implements MobSpawner {
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
                case "type" -> setSpawnedType(new BukkitEntityType(org.bukkit.entity.EntityType.valueOf(v.toUpperCase())));
                case "delay" -> setDelay(Integer.parseInt(v));
                case "min_delay" -> setMinSpawnDelay(Integer.parseInt(v));
                case "max_delay" -> setMaxSpawnDelay(Integer.parseInt(v));
                case "spawn_count" -> setSpawnCount(Integer.parseInt(v));
                case "spawn_range" -> setSpawnRange(Integer.parseInt(v));
                case "max_nearby" -> setMaxNearbyEntities(Integer.parseInt(v));
                case "required_player_range" -> setRequiredPlayerRange(Integer.parseInt(v));
                default -> throw new IllegalArgumentException("Invalid property: " + k);
            }
        });
    }
}
