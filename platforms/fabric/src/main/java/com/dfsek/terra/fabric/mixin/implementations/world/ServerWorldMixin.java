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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerWorld.class)
@Implements(@Interface(iface = World.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ServerWorldMixin {
    @Shadow
    public abstract long getSeed();

    public int terra$getMaxHeight() {
        return (((ServerWorld) (Object) this).getBottomY()) + ((ServerWorld) (Object) this).getHeight();
    }

    public ChunkGenerator terra$getGenerator() {
        return (ChunkGenerator) ((ServerWorld) (Object) this).getChunkManager().getChunkGenerator();
    }

    public Chunk terra$getChunkAt(int x, int z) {
        return (Chunk) ((ServerWorld) (Object) this).getChunk(x, z);
    }

    public com.dfsek.terra.api.block.state.BlockState terra$getBlockState(int x, int y, int z) {
        return FabricUtil.createState((WorldAccess) this, new BlockPos(x, y, z));
    }

    public BlockData terra$getBlockData(int x, int y, int z) {
        return new FabricBlockData(((ServerWorld) (Object) this).getBlockState(new BlockPos(x, y, z)));
    }

    public void terra$setBlockData(int x, int y, int z, BlockData data, boolean physics) {
        BlockPos pos = new BlockPos(x, y, z);
        ((ServerWorld) (Object) this).setBlockState(pos, ((FabricBlockData) data).getHandle(), physics ? 3 : 1042);
    }

    public Entity terra$spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(((ServerWorld) (Object) this));
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((ServerWorld) (Object) this).spawnEntity(entity);
        return (Entity) entity;
    }

    @Intrinsic
    public long terra$getSeed() {
        return getSeed();
    }

    public int terra$getMinHeight() {
        return ((ServerWorld) (Object) this).getBottomY();
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
     * Overridden in the same manner as {@link ChunkRegionMixin#hashCode()}
     *
     * @param other Another object
     * @return Whether this world is the same as other.
     * @see ChunkRegionMixin#hashCode()
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof ServerWorldAccess)) return false;
        return (ServerWorldAccess) this == (((ServerWorldAccess) other).toServerWorld());
    }
}
