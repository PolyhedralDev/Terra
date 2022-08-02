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

package com.dfsek.terra.mod.mixin.implementations.terra.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.MobSpawnerLogic;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.SerialState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.mixin.access.MobSpawnerLogicAccessor;


@Mixin(MobSpawnerBlockEntity.class)
@Implements(@Interface(iface = MobSpawner.class, prefix = "terra$"))
public abstract class MobSpawnerBlockEntityMixin extends BlockEntity {
    private MobSpawnerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Shadow
    public abstract MobSpawnerLogic getLogic();
    
    public EntityType terra$getSpawnedType() {
        return (EntityType) Registry.ENTITY_TYPE.get(
                Identifier.tryParse(((MobSpawnerLogicAccessor) getLogic()).getSpawnEntry().getNbt().getString("id")));
    }
    
    public void terra$setSpawnedType(@NotNull EntityType creatureType) {
        getLogic().setEntityId((net.minecraft.entity.EntityType<?>) creatureType);
    }
    
    public int terra$getDelay() {
        return 0;
    }
    
    public void terra$setDelay(int delay) {

    }
    
    public int terra$getMinSpawnDelay() {
        return 0;
    }
    
    public void terra$setMinSpawnDelay(int delay) {

    }
    
    public int terra$getMaxSpawnDelay() {
        return 0;
    }
    
    public void terra$setMaxSpawnDelay(int delay) {

    }
    
    public int terra$getSpawnCount() {
        return 0;
    }
    
    public void terra$setSpawnCount(int spawnCount) {

    }
    
    public int terra$getMaxNearbyEntities() {
        return 0;
    }
    
    public void terra$setMaxNearbyEntities(int maxNearbyEntities) {

    }
    
    public int terra$getRequiredPlayerRange() {
        return 0;
    }
    
    public void terra$setRequiredPlayerRange(int requiredPlayerRange) {

    }
    
    public int terra$getSpawnRange() {
        return 0;
    }
    
    public void terra$setSpawnRange(int spawnRange) {

    }
    
    public void terra$applyState(String state) {
        SerialState.parse(state).forEach((k, v) -> {
            switch(k) {
                case "type" -> terra$setSpawnedType(CommonPlatform.get().getWorldHandle().getEntity(v));
                case "delay" -> terra$setDelay(Integer.parseInt(v));
                case "min_delay" -> terra$setMinSpawnDelay(Integer.parseInt(v));
                case "max_delay" -> terra$setMaxSpawnDelay(Integer.parseInt(v));
                case "spawn_count" -> terra$setSpawnCount(Integer.parseInt(v));
                case "spawn_range" -> terra$setSpawnRange(Integer.parseInt(v));
                case "max_nearby" -> terra$setMaxNearbyEntities(Integer.parseInt(v));
                case "required_player_range" -> terra$setRequiredPlayerRange(Integer.parseInt(v));
                default -> throw new IllegalArgumentException("Invalid property: " + k);
            }
        });
    }
}
