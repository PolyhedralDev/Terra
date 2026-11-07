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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStep;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.WorldGenTickAccess;
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


@Mixin(WorldGenRegion.class)
@Implements(@Interface(iface = ProtoWorld.class, prefix = "terraWorld$"))
public abstract class WorldGenRegionMixin implements WorldGenLevel {
    private ConfigPack terra$config;


    @Shadow
    @Final
    private net.minecraft.server.level.ServerLevel level;

    @Shadow
    @Final
    private long seed;
    @Shadow
    @Final
    private ChunkAccess center;

    @Shadow
    @Final
    private WorldGenTickAccess<Fluid> fluidTicks;

    @Shadow
    @Final
    private WorldGenTickAccess<Block> blockTicks;


    @Inject(at = @At("RETURN"),
            method = "<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/StaticCache2D;Lnet/minecraft/world/level/chunk/status/ChunkStep;Lnet/minecraft/world/level/chunk/ChunkAccess;)V")
    public void injectConstructor(net.minecraft.server.level.ServerLevel world, StaticCache2D chunks,
                                  ChunkStep generationStep, ChunkAccess centerPos, CallbackInfo ci) {
        this.terra$config = ((ServerWorld) world).getPack();
    }


    @Intrinsic(displace = true)
    public void terraWorld$setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos blockPos = new BlockPos(x, y, z);
        net.minecraft.world.level.block.state.BlockState state;

        int flags = physics ? 3 : 1042;
        boolean isExtended = MinecraftUtil.isCompatibleBlockStateExtended(data);

        if(isExtended) {
            BlockInput arg = ((BlockInput) data);
            state = arg.getState();
            setBlock(blockPos, state, flags);
            net.minecraft.world.level.chunk.ChunkAccess chunk = getChunk(blockPos);
            CompoundTag nbt = ((CompoundTag) (Object) ((BlockStateExtended) data).getData());
            MinecraftUtil.loadBlockEntity(chunk, level, blockPos, state, nbt);
        } else {
            state = (net.minecraft.world.level.block.state.BlockState) data;
            setBlock(blockPos, state, flags);
        }

        if(physics) {
            MinecraftUtil.schedulePhysics(state, blockPos, getFluidTicks(), getBlockTicks());
        }
    }

    @Intrinsic
    public long terraWorld$getSeed() {
        return seed;
    }

    public int terraWorld$getMaxHeight() {
        return level.getMaxY();
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
        return level.getMinY();
    }

    public ChunkGenerator terraWorld$getGenerator() {
        return ((MinecraftChunkGeneratorWrapper) level.getChunkSource().getGenerator()).getHandle();
    }

    public BiomeProvider terraWorld$getBiomeProvider() {
        return terra$config.getBiomeProvider();
    }

    @SuppressWarnings("DataFlowIssue")
    public Entity terraWorld$spawnEntity(double x, double y, double z, EntityType data) {
        boolean isExtended = MinecraftUtil.isCompatibleEntityTypeExtended(data);
        net.minecraft.world.entity.Entity entity;
        if(isExtended) {
            MinecraftEntityTypeExtended type = ((MinecraftEntityTypeExtended) data);
            CompoundTag nbt = (CompoundTag) ((Object) type.getData());
            entity = net.minecraft.world.entity.EntityType.loadEntityRecursive(nbt, level, EntitySpawnReason.CHUNK_GENERATION, (entityx) -> {
                entityx.snapTo(x, y, z, entityx.getYRot(), entityx.getXRot());
                return entityx;
            });
            addFreshEntity(entity);
        } else {
            entity = ((net.minecraft.world.entity.EntityType<?>) data).create(level, EntitySpawnReason.CHUNK_GENERATION);
            entity.setPosRaw(x, y, z);
            addFreshEntity(entity);
        }

        return (Entity) entity;
    }

    public int terraWorld$centerChunkX() {
        return center.getPos().x;
    }

    public int terraWorld$centerChunkZ() {
        return center.getPos().z;
    }

    public ServerWorld terraWorld$getWorld() {
        return (ServerWorld) level;
    }

    public ConfigPack terraWorld$getPack() {
        return terra$config;
    }
}
