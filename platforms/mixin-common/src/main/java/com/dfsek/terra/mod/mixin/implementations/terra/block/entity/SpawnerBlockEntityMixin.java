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

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.SerialState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.implmentation.MinecraftEntityTypeExtended;
import com.dfsek.terra.mod.mixin.access.BaseSpawnerAccessor;


@Mixin(SpawnerBlockEntity.class)
@Implements(@Interface(iface = MobSpawner.class, prefix = "terra$"))
public abstract class SpawnerBlockEntityMixin extends BlockEntity {
    private SpawnerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow
    public abstract BaseSpawner getSpawner();

    //method_46408
    @Shadow
    public abstract void setEntityId(net.minecraft.world.entity.EntityType<?> entityType, RandomSource random);

    public EntityType terra$getSpawnedType() {
        return (EntityType) BuiltInRegistries.ENTITY_TYPE.get(
                Identifier.tryParse(((BaseSpawnerAccessor) getSpawner()).getSpawnEntry().getEntityToSpawn().getString("id").orElseThrow()))
            .orElseThrow();
    }

    public void terra$setSpawnedType(@NotNull EntityType creatureType) {
        RandomSource rand;
        if(hasLevel()) {
            rand = level.getRandom();
        } else {
            rand = RandomSource.create();
        }
        net.minecraft.world.entity.EntityType<?> entityType =
            (((net.minecraft.world.entity.EntityType<?>) (creatureType.isExtended() && creatureType.getClass().equals(
                MinecraftEntityTypeExtended.class) ? ((MinecraftEntityTypeExtended) creatureType).getType() : creatureType)));
        setEntityId(entityType, rand);
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
