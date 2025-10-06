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

import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettesFactory;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.gen.chunk.BlendingData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;


@Mixin(net.minecraft.world.chunk.ProtoChunk.class)
@Implements(@Interface(iface = ProtoChunk.class, prefix = "terra$"))
public abstract class ProtoChunkMixin extends Chunk {
    public ProtoChunkMixin(ChunkPos pos, UpgradeData upgradeData, HeightLimitView heightLimitView, PalettesFactory palettesFactory,
                           long inhabitedTime, @Nullable ChunkSection[] sectionArray, @Nullable BlendingData blendingData) {
        super(pos, upgradeData, heightLimitView, palettesFactory, inhabitedTime, sectionArray, blendingData);
    }

    @Shadow
    public abstract net.minecraft.block.BlockState getBlockState(BlockPos pos);

    @Shadow
    public abstract HeightLimitView getHeightLimitView();

    public void terra$setBlock(int x, int y, int z, @NotNull BlockState data) {
        BlockPos blockPos = new BlockPos(x, y, z);
        boolean isExtended = data.isExtended() && data.getClass().equals(BlockStateArgument.class);
        if(isExtended) {
            BlockStateExtended blockStateExtended = (BlockStateExtended) data;

            net.minecraft.block.BlockState blockState = (net.minecraft.block.BlockState) blockStateExtended.getState();
            this.setBlockState(blockPos, blockState, 0);
        } else {
            this.setBlockState(blockPos, (net.minecraft.block.BlockState) data, 0);
        }
    }

    public @NotNull BlockState terra$getBlock(int x, int y, int z) {
        return (BlockState) getBlockState(new BlockPos(x, y, z));
    }

    public int terra$getMaxHeight() {
        return getHeightLimitView().getTopYInclusive();
    }
}
