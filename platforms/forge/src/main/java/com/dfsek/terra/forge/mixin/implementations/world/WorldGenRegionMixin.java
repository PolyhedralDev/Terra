package com.dfsek.terra.forge.mixin.implementations.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.forge.block.ForgeBlock;
import com.dfsek.terra.forge.generation.ForgeChunkGeneratorWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldGenRegion.class)
@Implements(@Interface(iface = World.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class WorldGenRegionMixin {
    @Shadow
    @Final
    private ServerWorld level;

    @Shadow
    @Final
    private long seed;

    public int terra$getMaxHeight() {
        return ((WorldGenRegion) (Object) this).getMaxBuildHeight();
    }

    @SuppressWarnings("deprecation")
    public ChunkGenerator terra$getGenerator() {
        return (ChunkGenerator) ((WorldGenRegion) (Object) this).getLevel().getChunkSource().getGenerator();
    }

    public Chunk terra$getChunkAt(int x, int z) {
        return (Chunk) ((WorldGenRegion) (Object) this).getChunk(x, z);
    }

    public Block terra$getBlockAt(int x, int y, int z) {
        return new ForgeBlock(new BlockPos(x, y, z), ((WorldGenRegion) (Object) this));
    }

    @SuppressWarnings("deprecation")
    public Entity terra$spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(((WorldGenRegion) (Object) this).getLevel());
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((WorldGenRegion) (Object) this).addFreshEntity(entity);
        return (Entity) entity;
    }

    @Intrinsic
    public long terra$getSeed() {
        return seed;
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
        return level.hashCode();
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
        if(!(other instanceof WorldGenRegion)) return false;
        return level.equals(((WorldGenRegion) other).getLevel());
    }
}
