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

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.fabric.generation.BiomeProviderHolder;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import com.dfsek.terra.fabric.util.FabricUtil;

import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.tick.MultiTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = ProtoWorld.class, prefix = "terraWorld$"))
public abstract class ChunkRegionMixin {
    private ConfigPack config;
    
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
    
    @SuppressWarnings("deprecation")
    private final Lazy<BiomeProvider> caching = Lazy.lazy(() -> ((TerraBiomeSource) ((ChunkRegion) (Object) this)
            .toServerWorld()
            .getChunkManager()
            .getChunkGenerator()
            .getBiomeSource()).getProvider().caching((ProtoWorld) this));
    
    @Inject(at = @At("RETURN"),
            method = "<init>(Lnet/minecraft/server/world/ServerWorld;Ljava/util/List;Lnet/minecraft/world/chunk/ChunkStatus;I)V")
    public void injectConstructor(net.minecraft.server.world.ServerWorld world, List<net.minecraft.world.chunk.Chunk> list,
                                  ChunkStatus chunkStatus, int i,
                                  CallbackInfo ci) {
        this.config = ((ServerWorld) world).getPack();
    }
    
    public Entity terraWorld$spawnEntity(Vector3 location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(null);
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((ChunkRegion) (Object) this).spawnEntity(entity);
        return (Entity) entity;
    }
    
    @Intrinsic(displace = true)
    public void terraWorld$setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos pos = new BlockPos(x, y, z);
        ((ChunkRegion) (Object) this).setBlockState(pos, (net.minecraft.block.BlockState) data, physics ? 3 : 1042);
        if(physics && ((net.minecraft.block.BlockState) data).getBlock() instanceof FluidBlock) {
            fluidTickScheduler.scheduleTick(
                    OrderedTick.create(((FluidBlock) ((net.minecraft.block.BlockState) data).getBlock()).getFluidState(
                            (net.minecraft.block.BlockState) data).getFluid(), pos));
        }
    }
    
    @Intrinsic
    public long terraWorld$getSeed() {
        return seed;
    }
    
    public int terraWorld$getMaxHeight() {
        return (((ChunkRegion) (Object) this).getBottomY()) + ((ChunkRegion) (Object) this).getHeight();
    }
    
    @Intrinsic(displace = true)
    public BlockState terraWorld$getBlockState(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return (BlockState) ((ChunkRegion) (Object) this).getBlockState(pos);
    }
    
    public BlockEntity terraWorld$getBlockEntity(int x, int y, int z) {
        return FabricUtil.createState((WorldAccess) this, new BlockPos(x, y, z));
    }
    
    public int terraWorld$getMinHeight() {
        return ((ChunkRegion) (Object) this).getBottomY();
    }
    
    public ChunkGenerator terraWorld$getGenerator() {
        return ((FabricChunkGeneratorWrapper) world.getChunkManager().getChunkGenerator()).getHandle();
    }
    
    public BiomeProvider terraWorld$getBiomeProvider() {
        BiomeProvider provider = ((BiomeProviderHolder) this).getBiomeProvider();
        if(provider != null) {
            return provider;
        }
        return caching.value();
    }
    
    public Entity terraWorld$spawnEntity(double x, double y, double z, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(null);
        entity.setPos(x, y, z);
        ((ChunkRegion) (Object) this).spawnEntity(entity);
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
        return config;
    }
}
