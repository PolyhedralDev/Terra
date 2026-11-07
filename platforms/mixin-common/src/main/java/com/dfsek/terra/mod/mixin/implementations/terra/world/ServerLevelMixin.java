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

import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.ticks.LevelTicks;
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
import com.dfsek.terra.mod.mixin.access.LevelChunkAccessor;
import com.dfsek.terra.mod.util.MinecraftUtil;


@Mixin(net.minecraft.server.level.ServerLevel.class)
@Implements(@Interface(iface = ServerWorld.class, prefix = "terra$"))
public abstract class ServerLevelMixin extends Level {
    protected ServerLevelMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager,
                               Holder<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed,
                               int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Shadow
    public abstract LevelTicks<Block> getBlockTicks();

    @Shadow
    public abstract LevelTicks<Fluid> getFluidTicks();


    public Entity terra$spawnEntity(double x, double y, double z, EntityType data) {
        boolean isExtended = MinecraftUtil.isCompatibleEntityTypeExtended(data);
        net.minecraft.world.entity.Entity entity;
        if(isExtended) {
            MinecraftEntityTypeExtended type = ((MinecraftEntityTypeExtended) data);
            CompoundTag nbt = (CompoundTag) ((Object) type.getData());
            entity = net.minecraft.world.entity.EntityType.loadEntityRecursive(nbt, this, EntitySpawnReason.CHUNK_GENERATION, (entityx) -> {
                entityx.snapTo(x, y, z, entityx.getYRot(), entityx.getXRot());
                return entityx;
            });
            addFreshEntity(entity);
        } else {
            entity = ((net.minecraft.world.entity.EntityType<?>) data).create(this, EntitySpawnReason.CHUNK_GENERATION);
            entity.setPosRaw(x, y, z);
            addFreshEntity(entity);
        }

        return (Entity) entity;
    }

    public void terra$setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos blockPos = new BlockPos(x, y, z);
        net.minecraft.world.level.block.state.BlockState state;

        int flags = physics ? 3 : 1042;
        boolean isExtended = MinecraftUtil.isCompatibleBlockStateExtended(data);

        if(isExtended) {
            BlockInput arg = ((BlockInput) data);
            state = arg.getState();
            setBlock(blockPos, state, flags);
            net.minecraft.world.level.chunk.ChunkAccess chunk = getChunkAt(blockPos);
            ((LevelChunkAccessor) chunk).invokeLoadBlockEntity(blockPos, ((CompoundTag) (Object) ((BlockStateExtended) data).getData()));
        } else {
            state = (net.minecraft.world.level.block.state.BlockState) data;
            setBlock(blockPos, state, flags);
        }

        if(physics) {
            MinecraftUtil.schedulePhysics(state, blockPos, this.getFluidTicks(), this.getBlockTicks());
        }
    }


    @Intrinsic
    public long terra$getSeed() {
        return ((net.minecraft.server.level.ServerLevel) (Object) this).getSeed();
    }

    public int terra$getMaxHeight() {
        return ((this).getMinY()) +
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
        return (this).getMinY();
    }

    public ChunkGenerator terra$getGenerator() {
        return ((MinecraftChunkGeneratorWrapper) ((net.minecraft.server.level.ServerLevel) (Object) this).getChunkSource()
            .getGenerator()).getHandle();
    }

    public BiomeProvider terra$getBiomeProvider() {
        return ((TerraBiomeSource) ((net.minecraft.server.level.ServerLevel) (Object) this).getChunkSource()
            .getGenerator()
            .getBiomeSource()).getProvider();
    }

    public ConfigPack terra$getPack() {
        net.minecraft.world.level.chunk.ChunkGenerator generator =
            (((net.minecraft.server.level.ServerLevel) (Object) this).getChunkSource()).getGenerator();
        if(generator instanceof MinecraftChunkGeneratorWrapper minecraftChunkGeneratorWrapper) {
            return minecraftChunkGeneratorWrapper.getPack();
        }
        return null;
    }
}
