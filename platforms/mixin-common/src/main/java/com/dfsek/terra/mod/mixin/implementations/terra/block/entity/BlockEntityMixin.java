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

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;


@Mixin(net.minecraft.block.entity.BlockEntity.class)
@Implements(@Interface(iface = BlockEntity.class, prefix = "terra$"))
public abstract class BlockEntityMixin {
    public boolean terra$update(boolean applyPhysics) {
        if(((net.minecraft.block.entity.BlockEntity) (Object) this).hasWorld()) //noinspection ConstantConditions
            ((net.minecraft.block.entity.BlockEntity) (Object) this).getWorld().getChunk(
                    ((net.minecraft.block.entity.BlockEntity) (Object) this).getPos()).setBlockEntity(
                    (net.minecraft.block.entity.BlockEntity) (Object) this);
        return true;
    }
    
    public int terra$getX() {
        return ((net.minecraft.block.entity.BlockEntity) (Object) this).getPos().getX();
    }
    
    public int terra$getY() {
        return ((net.minecraft.block.entity.BlockEntity) (Object) this).getPos().getY();
    }
    
    public int terra$getZ() {
        return ((net.minecraft.block.entity.BlockEntity) (Object) this).getPos().getZ();
    }
    
    public BlockState terra$getBlockState() {
        return (BlockState) ((net.minecraft.block.entity.BlockEntity) (Object) this).getCachedState();
    }
}
