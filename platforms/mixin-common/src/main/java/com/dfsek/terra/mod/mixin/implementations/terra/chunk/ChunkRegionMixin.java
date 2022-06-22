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
import net.minecraft.world.ChunkRegion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.chunk.Chunk;


@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terraChunk$"))
public abstract class ChunkRegionMixin {
    
    @Shadow
    @Final
    private net.minecraft.world.chunk.Chunk centerPos;
    
    public void terraChunk$setBlock(int x, int y, int z, @NotNull BlockState blockState, boolean physics) {
        ((ChunkRegion) (Object) this).setBlockState(new BlockPos(x + (centerPos.getPos().x << 4), y, z + (centerPos.getPos().z << 4)),
                                                    (net.minecraft.block.BlockState) blockState, 0);
    }
    
    public @NotNull BlockState terraChunk$getBlock(int x, int y, int z) {
        return (BlockState) ((ChunkRegion) (Object) this).getBlockState(
                new BlockPos(x + (centerPos.getPos().x << 4), y, z + (centerPos.getPos().z << 4)));
    }
    
    public int terraChunk$getX() {
        return centerPos.getPos().x;
    }
    
    public int terraChunk$getZ() {
        return centerPos.getPos().z;
    }
}
