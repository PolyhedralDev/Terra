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

package com.dfsek.terra.mod.mixin.implementations.terra.world;

import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.mod.mixin.invoke.FluidBlockInvoker;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.FluidBlock;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.entity.SpawnReason;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.tick.MultiTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;
import com.dfsek.terra.mod.generation.TerraBiomeSource;
import com.dfsek.terra.mod.util.MinecraftUtil;

import org.spongepowered.asm.mixin.Shadow;


@Mixin(net.minecraft.server.world.ServerWorld.class)
@Implements(@Interface(iface = ServerWorld.class, prefix = "terra$"))
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager,
                               RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed,
                               int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Shadow
    public abstract WorldTickScheduler<Block> getBlockTickScheduler();

    @Shadow
    public abstract WorldTickScheduler<Fluid> getFluidTickScheduler();


    public Entity terra$spawnEntity(double x, double y, double z, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(null, SpawnReason.CHUNK_GENERATION);
        entity.setPos(x, y, z);
        spawnEntity(entity);
        return (Entity) entity;
    }

    public void terra$setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos blockPos = new BlockPos(x, y, z);
        int flags = physics ? 3 : 1042;
        boolean isExtended = data.isExtended() && data.getClass().equals(BlockStateArgument.class);
        if (isExtended) {
            BlockStateArgument arg = ((BlockStateArgument) data);
            net.minecraft.block.BlockState state = arg.getBlockState();
            setBlockState(blockPos, state, flags);
            net.minecraft.world.chunk.Chunk chunk = getChunk(blockPos);
            net.minecraft.block.entity.BlockEntity blockEntity;
            NbtCompound nbt = ((NbtCompound) (Object) ((BlockStateExtended)data).getData());
            if ("DUMMY".equals(nbt.getString("id", ""))) {
                if (state.hasBlockEntity()) {
                    blockEntity = ((BlockEntityProvider)state.getBlock()).createBlockEntity(blockPos, state);
                } else {
                    blockEntity = null;
                }
            } else {
                blockEntity = net.minecraft.block.entity.BlockEntity.createFromNbt(blockPos, state, nbt, getRegistryManager());
            }

            if (blockEntity != null) {
                blockEntity.setWorld(this);
                chunk.setBlockEntity(blockEntity);
            }
        } else {
            setBlockState(blockPos, (net.minecraft.block.BlockState) data, flags);
        }

        if(physics) {
            net.minecraft.block.BlockState state = isExtended ? ((BlockStateArgument) data).getBlockState() : ((net.minecraft.block.BlockState) data);
            if(state.isLiquid()) {
                getFluidTickScheduler().scheduleTick(OrderedTick.create(state.getFluidState().getFluid(), blockPos));
            } else {
                getBlockTickScheduler().scheduleTick(OrderedTick.create(state.getBlock(), blockPos));
            }
        }
    }

    @Intrinsic
    public long terra$getSeed() {
        return ((net.minecraft.server.world.ServerWorld) (Object) this).getSeed();
    }

    public int terra$getMaxHeight() {
        return (((net.minecraft.server.world.ServerWorld) (Object) this).getBottomY()) +
               ((net.minecraft.server.world.ServerWorld) (Object) this).getHeight();
    }

    public Chunk terra$getChunkAt(int x, int z) {
        return (Chunk) ((net.minecraft.server.world.ServerWorld) (Object) this).getChunk(x, z);
    }

    public BlockState terra$getBlockState(int x, int y, int z) {
        return (BlockState) ((net.minecraft.server.world.ServerWorld) (Object) this).getBlockState(new BlockPos(x, y, z));
    }

    public BlockEntity terra$getBlockEntity(int x, int y, int z) {
        return MinecraftUtil.createState((WorldAccess) this, new BlockPos(x, y, z));
    }

    public int terra$getMinHeight() {
        return ((net.minecraft.server.world.ServerWorld) (Object) this).getBottomY();
    }

    public ChunkGenerator terra$getGenerator() {
        return ((MinecraftChunkGeneratorWrapper) ((net.minecraft.server.world.ServerWorld) (Object) this).getChunkManager()
            .getChunkGenerator()).getHandle();
    }

    public BiomeProvider terra$getBiomeProvider() {
        return ((TerraBiomeSource) ((net.minecraft.server.world.ServerWorld) (Object) this).getChunkManager()
            .getChunkGenerator()
            .getBiomeSource()).getProvider();
    }

    public ConfigPack terra$getPack() {
        net.minecraft.world.gen.chunk.ChunkGenerator generator =
            (((net.minecraft.server.world.ServerWorld) (Object) this).getChunkManager()).getChunkGenerator();
        if(generator instanceof MinecraftChunkGeneratorWrapper minecraftChunkGeneratorWrapper) {
            return minecraftChunkGeneratorWrapper.getPack();
        }
        return null;
    }
}
