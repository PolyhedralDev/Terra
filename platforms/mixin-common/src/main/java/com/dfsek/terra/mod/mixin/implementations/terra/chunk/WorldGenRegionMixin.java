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

import com.dfsek.seismic.math.coord.CoordFunctions;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.WorldGenTickAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.mod.util.MinecraftUtil;


@Mixin(WorldGenRegion.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terraChunk$"))
public abstract class WorldGenRegionMixin implements WorldGenLevel {

    @Shadow
    @Final
    private net.minecraft.world.level.chunk.ChunkAccess center;

    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    @Final
    private WorldGenTickAccess<Block> blockTicks;

    @Shadow
    @Final
    private WorldGenTickAccess<Fluid> fluidTicks;

    @Shadow
    public abstract net.minecraft.world.level.block.state.BlockState getBlockState(BlockPos pos);

    @Shadow
    @Nullable
    public abstract boolean setBlock(BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags, int maxUpdateDepth);


    public void terraChunk$setBlock(int x, int y, int z, @NotNull BlockState data, boolean physics) {
        ChunkPos pos = center.getPos();
        BlockPos blockPos = new BlockPos(CoordFunctions.chunkAndRelativeToAbsolute(pos.x, x), y,
            CoordFunctions.chunkAndRelativeToAbsolute(pos.z, z));
        net.minecraft.world.level.block.state.BlockState state;

        boolean isExtended = MinecraftUtil.isCompatibleBlockStateExtended(data);

        if(isExtended) {
            BlockInput arg = ((BlockInput) data);
            state = arg.getState();
            setBlock(blockPos, state, 0, 512);
            net.minecraft.world.level.chunk.ChunkAccess chunk = getChunk(blockPos);
            CompoundTag nbt = ((CompoundTag) (Object) ((BlockStateExtended) data).getData());
            MinecraftUtil.loadBlockEntity(chunk, level, blockPos, state, nbt);
        } else {
            state = (net.minecraft.world.level.block.state.BlockState) data;
            setBlock(blockPos, state, 0, 512);
        }

        if(physics) {
            MinecraftUtil.schedulePhysics(state, blockPos, getFluidTicks(), getBlockTicks());
        }
    }

    public @NotNull BlockState terraChunk$getBlock(int x, int y, int z) {
        return (BlockState) ((WorldGenRegion) (Object) this).getBlockState(
            new BlockPos(x + (center.getPos().x << 4), y, z + (center.getPos().z << 4)));
    }

    public int terraChunk$getX() {
        return center.getPos().x;
    }

    public int terraChunk$getZ() {
        return center.getPos().z;
    }
}
