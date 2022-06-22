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

package com.dfsek.terra.mod.mixin.implementations.terra.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.mod.util.MinecraftAdapter;


@Mixin(Entity.class)
@Implements(@Interface(iface = com.dfsek.terra.api.entity.Entity.class, prefix = "terra$"))
public abstract class EntityMixin {
    @Shadow
    public net.minecraft.world.World world;
    
    @Shadow
    private BlockPos blockPos;
    
    @Shadow
    public abstract void teleport(double destX, double destY, double destZ);
    
    public Vector3 terra$position() {
        return MinecraftAdapter.adapt(blockPos);
    }
    
    public void terra$position(Vector3 location) {
        teleport(location.getX(), location.getY(), location.getZ());
    }
    
    public ServerWorld terra$world() {
        return (ServerWorld) world;
    }
}
