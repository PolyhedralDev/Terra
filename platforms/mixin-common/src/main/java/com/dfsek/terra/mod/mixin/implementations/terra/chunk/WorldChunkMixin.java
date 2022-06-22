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

package com.dfsek.terra.mod.mixin.implementations.terra.chunk;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.chunk.Chunk;


@Mixin(WorldChunk.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terra$"))
public abstract class WorldChunkMixin {
    @Final
    @Shadow
    net.minecraft.world.World world;
    
    @Shadow
    public abstract net.minecraft.block.BlockState getBlockState(BlockPos pos);
    
    @Shadow
    @Nullable
    public abstract net.minecraft.block.BlockState setBlockState(BlockPos pos, net.minecraft.block.BlockState state, boolean moved);
    
    public void terra$setBlock(int x, int y, int z, BlockState data, boolean physics) {
        setBlockState(new BlockPos(x, y, z), (net.minecraft.block.BlockState) data, false);
    }
    
    public void terra$setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        ((net.minecraft.world.chunk.Chunk) (Object) this).setBlockState(new BlockPos(x, y, z), (net.minecraft.block.BlockState) blockState,
                                                                        false);
    }
    
    @Intrinsic
    public @NotNull BlockState terra$getBlock(int x, int y, int z) {
        return (BlockState) getBlockState(new BlockPos(x, y, z));
    }
    
    public int terra$getX() {
        return ((net.minecraft.world.chunk.Chunk) (Object) this).getPos().x;
    }
    
    public int terra$getZ() {
        return ((net.minecraft.world.chunk.Chunk) (Object) this).getPos().z;
    }
    
    public ServerWorld terra$getWorld() {
        return (ServerWorld) world;
    }
}
