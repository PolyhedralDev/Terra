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

import net.minecraft.block.Block;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.entity.SpawnReason;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;
import com.dfsek.terra.mod.generation.TerraBiomeSource;
import com.dfsek.terra.mod.implmentation.MinecraftEntityTypeExtended;
import com.dfsek.terra.mod.mixin.access.WorldChunkAccessor;
import com.dfsek.terra.mod.util.MinecraftUtil;


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


    public Entity terra$spawnEntity(double x, double y, double z, EntityType data) {
        boolean isExtended = MinecraftUtil.isCompatibleEntityTypeExtended(data);
        net.minecraft.entity.Entity entity;
        if(isExtended) {
            MinecraftEntityTypeExtended type = ((MinecraftEntityTypeExtended) data);
            NbtCompound nbt = (NbtCompound) ((Object) type.getData());
            entity = net.minecraft.entity.EntityType.loadEntityWithPassengers(nbt, this, SpawnReason.CHUNK_GENERATION, (entityx) -> {
                entityx.refreshPositionAndAngles(x, y, z, entityx.getYaw(), entityx.getPitch());
                return entityx;
            });
            spawnEntity(entity);
        } else {
            entity = ((net.minecraft.entity.EntityType<?>) data).create(this, SpawnReason.CHUNK_GENERATION);
            entity.setPos(x, y, z);
            spawnEntity(entity);
        }

        return (Entity) entity;
    }

    public void terra$setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos blockPos = new BlockPos(x, y, z);
        net.minecraft.block.BlockState state;

        int flags = physics ? 3 : 1042;
        boolean isExtended = MinecraftUtil.isCompatibleBlockStateExtended(data);

        if(isExtended) {
            BlockStateArgument arg = ((BlockStateArgument) data);
            state = arg.getBlockState();
            setBlockState(blockPos, state, flags);
            net.minecraft.world.chunk.Chunk chunk = getWorldChunk(blockPos);
            ((WorldChunkAccessor) chunk).invokeLoadBlockEntity(blockPos, ((NbtCompound) (Object) ((BlockStateExtended) data).getData()));
        } else {
            state = (net.minecraft.block.BlockState) data;
            setBlockState(blockPos, state, flags);
        }

        if(physics) {
            MinecraftUtil.schedulePhysics(state, blockPos, this.getFluidTickScheduler(), this.getBlockTickScheduler());
        }
    }


    @Intrinsic
    public long terra$getSeed() {
        return ((net.minecraft.server.world.ServerWorld) (Object) this).getSeed();
    }

    public int terra$getMaxHeight() {
        return ((this).getBottomY()) +
               (this).getHeight();
    }

    public Chunk terra$getChunkAt(int x, int z) {
        return (Chunk) (this).getChunk(x, z);
    }

    public BlockState terra$getBlockState(int x, int y, int z) {
        return (BlockState) (this).getBlockState(new BlockPos(x, y, z));
    }

    public BlockEntity terra$getBlockEntity(int x, int y, int z) {
        return MinecraftUtil.createBlockEntity(this, new BlockPos(x, y, z));
    }

    public int terra$getMinHeight() {
        return (this).getBottomY();
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
