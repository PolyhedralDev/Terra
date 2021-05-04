package com.dfsek.terra.fabric.mixin.implementations.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.fabric.block.FabricBlock;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = World.class, prefix = "terra$"))
public abstract class ChunkRegionMixin {
    @Shadow
    @Final
    private ServerWorld world;

    public int terra$getMaxHeight() {
        return ((ChunkRegion) (Object) this).getDimensionHeight();
    }

    public ChunkGenerator terra$getGenerator() {
        return (ChunkGenerator) ((ChunkRegion) (Object) this).toServerWorld().getChunkManager().getChunkGenerator();
    }

    public Chunk terra$getChunkAt(int x, int z) {
        return (Chunk) ((ChunkRegion) (Object) this).getChunk(x, z);
    }

    public Block terra$getBlockAt(int x, int y, int z) {
        return new FabricBlock(new BlockPos(x, y, z), ((ChunkRegion) (Object) this));
    }

    public Entity terra$spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(((ChunkRegion) (Object) this).toServerWorld());
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((ChunkRegion) (Object) this).spawnEntity(entity);
        return (Entity) entity;
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
