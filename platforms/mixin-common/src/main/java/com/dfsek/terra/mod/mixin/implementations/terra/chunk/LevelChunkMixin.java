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

import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.mod.util.MinecraftUtil;


@Mixin(LevelChunk.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terra$"))
public abstract class LevelChunkMixin {
    @Final
    @Shadow
    net.minecraft.world.level.Level level;

    @Shadow
    public abstract net.minecraft.world.level.block.state.BlockState getBlockState(BlockPos pos);

    @Shadow
    @Nullable
    public abstract net.minecraft.world.level.block.state.BlockState setBlockState(BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags);

    @Shadow
    protected abstract BlockEntity promotePendingBlockEntity(BlockPos pos, CompoundTag nbt);

    @SuppressWarnings("ConstantValue")
    public void terra$setBlock(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos blockPos = new BlockPos(x, y, z);
        net.minecraft.world.level.block.state.BlockState state;

        boolean isExtended = MinecraftUtil.isCompatibleBlockStateExtended(data);

        if(isExtended) {
            BlockInput arg = ((BlockInput) data);
            state = arg.getState();
            setBlockState(blockPos, state, 0);
            promotePendingBlockEntity(blockPos, ((CompoundTag) (Object) ((BlockStateExtended) data).getData()));
        } else {
            state = (net.minecraft.world.level.block.state.BlockState) data;
            setBlockState(blockPos, state, 0);
        }

        if(physics) {
            MinecraftUtil.schedulePhysics(state, blockPos, level.getFluidTicks(), level.getBlockTicks());
        }
    }

    @SuppressWarnings("ConstantValue")
    public void terra$setBlock(int x, int y, int z, @NotNull BlockState data) {
        terra$setBlock(x, y, z, data, false);
    }

    @Intrinsic
    public @NotNull BlockState terra$getBlock(int x, int y, int z) {
        return (BlockState) getBlockState(new BlockPos(x, y, z));
    }

    public int terra$getX() {
        return ((net.minecraft.world.level.chunk.ChunkAccess) (Object) this).getPos().x;
    }

    public int terra$getZ() {
        return ((net.minecraft.world.level.chunk.ChunkAccess) (Object) this).getPos().z;
    }

    public ServerWorld terra$getWorld() {
        return (ServerWorld) level;
    }
}
