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

package com.dfsek.terra.fabric.mixin.implementations.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.fabric.util.FabricAdapter;


@Mixin(net.minecraft.block.entity.BlockEntity.class)
@Implements(@Interface(iface = BlockEntity.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class BlockEntityMixin {
    @Final
    @Shadow
    protected BlockPos pos;
    @Shadow
    @Nullable
    protected World world;
    
    @Shadow
    public abstract net.minecraft.block.BlockState getCachedState();
    
    @Shadow
    public abstract boolean hasWorld();
    
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
    
    public boolean terra$update(boolean applyPhysics) {
        if(hasWorld()) //noinspection ConstantConditions
            world.getChunk(pos).setBlockEntity((net.minecraft.block.entity.BlockEntity) (Object) this);
        return true;
    }
    
    public int terra$getX() {
        return pos.getX();
    }
    
    public int terra$getY() {
        return pos.getY();
    }
    
    public int terra$getZ() {
        return pos.getZ();
    }
    
    public BlockState terra$getBlockData() {
        return FabricAdapter.adapt(getCachedState());
    }
}
