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
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerationStep;
import net.minecraft.world.tick.MultiTickScheduler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;
import com.dfsek.terra.mod.implmentation.MinecraftEntityTypeExtended;
import com.dfsek.terra.mod.util.MinecraftUtil;


@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = ProtoWorld.class, prefix = "terraWorld$"))
public abstract class ChunkRegionMixin implements StructureWorldAccess {
    private ConfigPack terra$config;


    @Shadow
    @Final
    private net.minecraft.server.world.ServerWorld world;

    @Shadow
    @Final
    private long seed;
    @Shadow
    @Final
    private Chunk centerPos;

    @Shadow
    @Final
    private MultiTickScheduler<Fluid> fluidTickScheduler;

    @Shadow
    @Final
    private MultiTickScheduler<Block> blockTickScheduler;


    @Inject(at = @At("RETURN"),
            method = "<init>(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/collection/BoundedRegionArray;" +
                     "Lnet/minecraft/world/chunk/ChunkGenerationStep;Lnet/minecraft/world/chunk/Chunk;)V")
    public void injectConstructor(net.minecraft.server.world.ServerWorld world, BoundedRegionArray chunks,
                                  ChunkGenerationStep generationStep, Chunk centerPos, CallbackInfo ci) {
        this.terra$config = ((ServerWorld) world).getPack();
    }


    @Intrinsic(displace = true)
    public void terraWorld$setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos blockPos = new BlockPos(x, y, z);
        net.minecraft.block.BlockState state;

        int flags = physics ? 3 : 1042;
        boolean isExtended = MinecraftUtil.isCompatibleBlockStateExtended(data);

        if(isExtended) {
            BlockStateArgument arg = ((BlockStateArgument) data);
            state = arg.getBlockState();
            setBlockState(blockPos, state, flags);
            net.minecraft.world.chunk.Chunk chunk = getChunk(blockPos);
            NbtCompound nbt = ((NbtCompound) (Object) ((BlockStateExtended) data).getData());
            MinecraftUtil.loadBlockEntity(chunk, world, blockPos, state, nbt);
        } else {
            state = (net.minecraft.block.BlockState) data;
            setBlockState(blockPos, state, flags);
        }

        if(physics) {
            MinecraftUtil.schedulePhysics(state, blockPos, getFluidTickScheduler(), getBlockTickScheduler());
        }
    }

    @Intrinsic
    public long terraWorld$getSeed() {
        return seed;
    }

    public int terraWorld$getMaxHeight() {
        return world.getTopYInclusive();
    }

    @Intrinsic(displace = true)
    public BlockState terraWorld$getBlockState(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return (BlockState) (this).getBlockState(pos);
    }

    public BlockEntity terraWorld$getBlockEntity(int x, int y, int z) {
        return MinecraftUtil.createBlockEntity(this, new BlockPos(x, y, z));
    }

    public int terraWorld$getMinHeight() {
        return world.getBottomY();
    }

    public ChunkGenerator terraWorld$getGenerator() {
        return ((MinecraftChunkGeneratorWrapper) world.getChunkManager().getChunkGenerator()).getHandle();
    }

    public BiomeProvider terraWorld$getBiomeProvider() {
        return terra$config.getBiomeProvider();
    }

    @SuppressWarnings("DataFlowIssue")
    public Entity terraWorld$spawnEntity(double x, double y, double z, EntityType data) {
        boolean isExtended = MinecraftUtil.isCompatibleEntityTypeExtended(data);
        net.minecraft.entity.Entity entity;
        if(isExtended) {
            MinecraftEntityTypeExtended type = ((MinecraftEntityTypeExtended) data);
            NbtCompound nbt = (NbtCompound) ((Object) type.getData());
            entity = net.minecraft.entity.EntityType.loadEntityWithPassengers(nbt, world, SpawnReason.CHUNK_GENERATION, (entityx) -> {
                entityx.refreshPositionAndAngles(x, y, z, entityx.getYaw(), entityx.getPitch());
                return entityx;
            });
            spawnEntity(entity);
        } else {
            entity = ((net.minecraft.entity.EntityType<?>) data).create(world, SpawnReason.CHUNK_GENERATION);
            entity.setPos(x, y, z);
            spawnEntity(entity);
        }

        return (Entity) entity;
    }

    public int terraWorld$centerChunkX() {
        return centerPos.getPos().x;
    }

    public int terraWorld$centerChunkZ() {
        return centerPos.getPos().z;
    }

    public ServerWorld terraWorld$getWorld() {
        return (ServerWorld) world;
    }

    public ConfigPack terraWorld$getPack() {
        return terra$config;
    }
}
