package com.dfsek.terra.forge.mixin.implementations.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.forge.block.ForgeBlock;
import com.dfsek.terra.forge.generation.ForgeChunkGeneratorWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.server.ServerWorld;
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
        return ((ServerWorld) (Object) this).getMaxBuildHeight();
    }

    public ChunkGenerator terra$getGenerator() {
        return (ChunkGenerator) ((ServerWorld) (Object) this).getChunkSource().getGenerator();
    }

    public Chunk terra$getChunkAt(int x, int z) {
        return (Chunk) ((ServerWorld) (Object) this).getChunk(x, z);
    }

    public Block terra$getBlockAt(int x, int y, int z) {
        return new ForgeBlock(new BlockPos(x, y, z), ((ServerWorld) (Object) this));
    }

    public Entity terra$spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(((ServerWorld) (Object) this));
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((ServerWorld) (Object) this).addFreshEntity(entity);
        return (Entity) entity;
    }

    @Intrinsic
    public long terra$getSeed() {
        return getSeed();
    }

    public int terra$getMinHeight() {
        return 0;
    }

    public Object terra$getHandle() {
        return this;
    }

    public boolean terra$isTerraWorld() {
        return terra$getGenerator() instanceof GeneratorWrapper;
    }

    public TerraChunkGenerator terra$getTerraGenerator() {
        return ((ForgeChunkGeneratorWrapper) terra$getGenerator()).getHandle();
    }

    /**
     * Overridden in the same manner as {@link WorldGenRegionMixin#hashCode()}
     *
     * @param other Another object
     * @return Whether this world is the same as other.
     * @see WorldGenRegionMixin#hashCode()
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof IServerWorld)) return false;
        return (IServerWorld) this == (((IServerWorld) other).getLevel());
    }
}
