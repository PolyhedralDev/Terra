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

import com.dfsek.terra.api.block.state.BlockStateExtended;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.DimensionEffects.End;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.EndGatewayFeature;
import net.minecraft.world.tick.MultiTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.chunk.Chunk;


@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terraChunk$"))
public abstract class ChunkRegionMixin implements StructureWorldAccess {

    @Shadow
    @Final
    private net.minecraft.world.chunk.Chunk centerPos;

    @Shadow
    @Final
    private ServerWorld world;

    @Shadow
    @Final
    private MultiTickScheduler<Block> blockTickScheduler;

    @Shadow
    @Final
    private MultiTickScheduler<Fluid> fluidTickScheduler;

    @Shadow
    public abstract net.minecraft.block.BlockState getBlockState(BlockPos pos);

    @Shadow
    @Nullable
    public abstract boolean setBlockState(BlockPos pos, net.minecraft.block.BlockState state, int flags, int maxUpdateDepth);


    public void terraChunk$setBlock(int x, int y, int z, @NotNull BlockState data, boolean physics) {
        ChunkPos pos = centerPos.getPos();
        BlockPos blockPos = new BlockPos(CoordFunctions.chunkAndRelativeToAbsolute(pos.x, x), y, CoordFunctions.chunkAndRelativeToAbsolute(pos.z, z));
        boolean isExtended = data.isExtended() && data.getClass().equals(BlockStateArgument.class);
        if (isExtended) {
            BlockStateArgument arg = ((BlockStateArgument) data);
            net.minecraft.block.BlockState state = arg.getBlockState();
            setBlockState(blockPos, state, 0, 512);
            net.minecraft.world.chunk.Chunk chunk = getChunk(blockPos);
            BlockEntity blockEntity;
            NbtCompound nbt = ((NbtCompound) (Object) ((BlockStateExtended)data).getData());
            if ("DUMMY".equals(nbt.getString("id", ""))) {
                if (state.hasBlockEntity()) {
                    blockEntity = ((BlockEntityProvider)state.getBlock()).createBlockEntity(blockPos, state);
                } else {
                    blockEntity = null;
                }
            } else {
                blockEntity = BlockEntity.createFromNbt(blockPos, state, nbt, this.world.getRegistryManager());
            }

            if (blockEntity != null) {
                blockEntity.setWorld(this.world);
                chunk.setBlockEntity(blockEntity);
            }
        } else {
            setBlockState(blockPos, (net.minecraft.block.BlockState) data, 0, 512);
        }

        if(physics) {
            net.minecraft.block.BlockState state = isExtended ? ((BlockStateArgument) data).getBlockState() : ((net.minecraft.block.BlockState) data);
            if(state.isLiquid()) {
                fluidTickScheduler.scheduleTick(OrderedTick.create(state.getFluidState().getFluid(), blockPos));
            } else {
                blockTickScheduler.scheduleTick(OrderedTick.create(state.getBlock(), blockPos));
            }
        }
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
