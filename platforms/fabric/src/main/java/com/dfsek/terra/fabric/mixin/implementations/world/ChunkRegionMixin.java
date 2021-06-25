package com.dfsek.terra.fabric.mixin.implementations.world;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generator.TerraChunkGenerator;
import com.dfsek.terra.fabric.block.FabricBlockData;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.util.FabricUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.TickScheduler;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = World.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ChunkRegionMixin {
    @Shadow
    @Final
    private ServerWorld world;

    @Shadow
    @Final
    private long seed;

    @Shadow
    public abstract boolean setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth);

    @Shadow
    public abstract TickScheduler<Fluid> getFluidTickScheduler();

    public int terra$getMaxHeight() {
        return (((ChunkRegion) (Object) this).getBottomY()) + ((ChunkRegion) (Object) this).getHeight();
    }

    @SuppressWarnings("deprecation")
    public ChunkGenerator terra$getGenerator() {
        return (ChunkGenerator) ((ChunkRegion) (Object) this).toServerWorld().getChunkManager().getChunkGenerator();
    }

    public Chunk terra$getChunkAt(int x, int z) {
        return (Chunk) ((ChunkRegion) (Object) this).getChunk(x, z);
    }

    public com.dfsek.terra.api.block.state.BlockState terra$getBlockState(int x, int y, int z) {
        return FabricUtil.createState((WorldAccess) this, new BlockPos(x, y, z));
    }

    @SuppressWarnings("deprecation")
    public Entity terra$spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(((ChunkRegion) (Object) this).toServerWorld());
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((ChunkRegion) (Object) this).spawnEntity(entity);
        return (Entity) entity;
    }

    public BlockData terra$getBlockData(int x, int y, int z) {
        return new FabricBlockData(((ChunkRegion) (Object) this).getBlockState(new BlockPos(x, y, z)));
    }

    public void terra$setBlockData(int x, int y, int z, BlockData data, boolean physics) {
        BlockPos pos = new BlockPos(x, y, z);
        ((ChunkRegion) (Object) this).setBlockState(pos, ((FabricBlockData) data).getHandle(), physics ? 3 : 1042);
        if(physics && ((FabricBlockData) data).getHandle().getBlock() instanceof FluidBlock) {
            getFluidTickScheduler().schedule(pos, ((FluidBlock) ((FabricBlockData) data).getHandle().getBlock()).getFluidState(((FabricBlockData) data).getHandle()).getFluid(), 0);
        }
    }

    @Intrinsic
    public long terra$getSeed() {
        return seed;
    }

    public int terra$getMinHeight() {
        return ((ChunkRegion) (Object) this).getBottomY();
    }

    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }

    public boolean terra$isTerraWorld() {
        return terra$getGenerator() instanceof GeneratorWrapper;
    }

    public TerraChunkGenerator terra$getTerraGenerator() {
        return ((FabricChunkGeneratorWrapper) terra$getGenerator()).getHandle();
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
