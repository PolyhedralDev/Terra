package com.dfsek.terra.fabric.mixin.implementations.world;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.fabric.block.FabricBlockState;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import com.dfsek.terra.fabric.util.FabricUtil;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.TickScheduler;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.ChunkStatus;
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

@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = World.class, prefix = "terraWorld$", remap = Interface.Remap.NONE))
public abstract class ChunkRegionMixin {
    private WorldConfig config;

    @Shadow
    @Final
    private ServerWorld world;

    @Shadow
    @Final
    private long seed;

    @Shadow
    public abstract TickScheduler<Fluid> getFluidTickScheduler();

    public int terraWorld$getMaxHeight() {
        return (((ChunkRegion) (Object) this).getBottomY()) + ((ChunkRegion) (Object) this).getHeight();
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/server/world/ServerWorld;Ljava/util/List;Lnet/minecraft/world/chunk/ChunkStatus;I)V")
    public void injectConstructor(ServerWorld world, List<net.minecraft.world.chunk.Chunk> list, ChunkStatus chunkStatus, int i, CallbackInfo ci) {
        this.config = ((World) world).getConfig();
    }

    public Chunk terraWorld$getChunkAt(int x, int z) {
        return (Chunk) ((ChunkRegion) (Object) this).getChunk(x, z);
    }

    public BlockEntity terraWorld$getBlockState(int x, int y, int z) {
        return FabricUtil.createState((WorldAccess) this, new BlockPos(x, y, z));
    }

    @SuppressWarnings("deprecation")
    public Entity terraWorld$spawnEntity(Vector3 location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(((ChunkRegion) (Object) this).toServerWorld());
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((ChunkRegion) (Object) this).spawnEntity(entity);
        return (Entity) entity;
    }

    @Intrinsic(displace = true)
    public BlockState terraWorld$getBlockData(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return new FabricBlockState(((ChunkRegion) (Object) this).getBlockState(pos));
    }

    @Intrinsic(displace = true)
    public void terraWorld$setBlockData(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos pos = new BlockPos(x, y, z);
        ((ChunkRegion) (Object) this).setBlockState(pos, ((FabricBlockState) data).getHandle(), physics ? 3 : 1042);
        if(physics && ((FabricBlockState) data).getHandle().getBlock() instanceof FluidBlock) {
            getFluidTickScheduler().schedule(pos, ((FluidBlock) ((FabricBlockState) data).getHandle().getBlock()).getFluidState(((FabricBlockState) data).getHandle()).getFluid(), 0);
        }
    }

    @Intrinsic
    public long terraWorld$getSeed() {
        return seed;
    }

    public int terraWorld$getMinHeight() {
        return ((ChunkRegion) (Object) this).getBottomY();
    }

    @Intrinsic
    public Object terraWorld$getHandle() {
        return this;
    }

    public ChunkGenerator terraWorld$getGenerator() {
        return ((FabricChunkGeneratorWrapper) world.getChunkManager().getChunkGenerator()).getHandle();
    }

    @SuppressWarnings("deprecation")
    public BiomeProvider terraWorld$getBiomeProvider() {
        return ((TerraBiomeSource) ((ChunkRegion) (Object) this).toServerWorld().getChunkManager().getChunkGenerator().getBiomeSource()).getProvider();
    }

    public WorldConfig terraWorld$getConfig() {
        return config;
    }

    /**
     * We need regions delegating to the same world
     * to have the same hashcode. This
     * minimizes cache misses.
     * <p>
     * This is sort of jank, but shouldn't(tm)
     * break any other mods, unless they're doing
     * something they really shouldn't, since
     * ChunkRegions are not supposed to persist.
     */
    @Override
    public int hashCode() {
        return world.hashCode();
    }

    /**
     * Overridden in the same manner as {@link #hashCode()}
     *
     * @param other Another object
     * @return Whether this world is the same as other.
     * @see #hashCode()
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof ServerWorldAccess)) return false;
        return world.equals(((ServerWorldAccess) other).toServerWorld());
    }
}
