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

package com.dfsek.terra.mod.mixin.implementations.terra.chunk.data;

import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerFactory;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;


@Mixin(net.minecraft.world.level.chunk.ProtoChunk.class)
@Implements(@Interface(iface = ProtoChunk.class, prefix = "terra$"))
public abstract class ProtoChunkMixin extends ChunkAccess {
    public ProtoChunkMixin(ChunkPos pos, UpgradeData upgradeData, LevelHeightAccessor heightLimitView, PalettedContainerFactory palettesFactory,
                           long inhabitedTime, @Nullable LevelChunkSection[] sectionArray, @Nullable BlendingData blendingData) {
        super(pos, upgradeData, heightLimitView, palettesFactory, inhabitedTime, sectionArray, blendingData);
    }

    @Shadow
    public abstract net.minecraft.world.level.block.state.BlockState getBlockState(BlockPos pos);

    @Shadow
    public abstract LevelHeightAccessor getHeightAccessorForGeneration();

    public void terra$setBlock(int x, int y, int z, @NotNull BlockState data) {
        BlockPos blockPos = new BlockPos(x, y, z);
        boolean isExtended = data.isExtended() && data.getClass().equals(BlockInput.class);
        if(isExtended) {
            BlockStateExtended blockStateExtended = (BlockStateExtended) data;

            net.minecraft.world.level.block.state.BlockState blockState = (net.minecraft.world.level.block.state.BlockState) blockStateExtended.getState();
            this.setBlockState(blockPos, blockState, 0);
        } else {
            this.setBlockState(blockPos, (net.minecraft.world.level.block.state.BlockState) data, 0);
        }
    }

    public @NotNull BlockState terra$getBlock(int x, int y, int z) {
        return (BlockState) getBlockState(new BlockPos(x, y, z));
    }

    public int terra$getMaxHeight() {
        return getHeightAccessorForGeneration().getMaxY();
    }
}
