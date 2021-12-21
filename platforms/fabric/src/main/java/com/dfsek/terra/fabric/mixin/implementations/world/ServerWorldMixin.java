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

package com.dfsek.terra.fabric.mixin.implementations.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import com.dfsek.terra.fabric.util.FabricUtil;


@Mixin(net.minecraft.server.world.ServerWorld.class)
@Implements(@Interface(iface = ServerWorld.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ServerWorldMixin {
    private ConfigPack config;
    @Shadow
    @Final
    private ServerChunkManager chunkManager;
    
    @Shadow
    public abstract long getSeed();
    
    @Inject(at = @At("RETURN"),
            method = "<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;" +
                     "Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/level/ServerWorldProperties;" +
                     "Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/world/dimension/DimensionType;" +
                     "Lnet/minecraft/server/WorldGenerationProgressListener;Lnet/minecraft/world/gen/chunk/ChunkGenerator;" +
                     "ZJLjava/util/List;Z)V")
    public void injectConstructor(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session,
                                  ServerWorldProperties properties, RegistryKey<net.minecraft.world.World> worldKey,
                                  DimensionType dimensionType, WorldGenerationProgressListener worldGenerationProgressListener,
                                  net.minecraft.world.gen.chunk.ChunkGenerator chunkGenerator, boolean debugWorld, long seed,
                                  List<Spawner> spawners, boolean shouldTickTime, CallbackInfo ci) {
        if(chunkGenerator instanceof FabricChunkGeneratorWrapper) {
            config = ((FabricChunkGeneratorWrapper) chunkGenerator).getPack();
        }
    }
    
    public Entity terra$spawnEntity(Vector3 location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(
                ((net.minecraft.server.world.ServerWorld) (Object) this));
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((net.minecraft.server.world.ServerWorld) (Object) this).spawnEntity(entity);
        return (Entity) entity;
    }
    
    public void terra$setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos pos = new BlockPos(x, y, z);
        ((net.minecraft.server.world.ServerWorld) (Object) this).setBlockState(pos, (net.minecraft.block.BlockState) data,
                                                                               physics ? 3 : 1042);
    }
    
    @Intrinsic
    public long terra$getSeed() {
        return getSeed();
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
        return FabricUtil.createState((WorldAccess) this, new BlockPos(x, y, z));
    }
    
    public int terra$getMinHeight() {
        return ((net.minecraft.server.world.ServerWorld) (Object) this).getBottomY();
    }
    
    public ChunkGenerator terra$getGenerator() {
        return ((FabricChunkGeneratorWrapper) chunkManager.getChunkGenerator()).getHandle();
    }
    
    public BiomeProvider terra$getBiomeProvider() {
        return ((TerraBiomeSource) ((net.minecraft.server.world.ServerWorld) (Object) this).getChunkManager()
                                                                                           .getChunkGenerator()
                                                                                           .getBiomeSource()).getProvider();
    }
    
    public ConfigPack terra$getPack() {
        return config;
    }
}
