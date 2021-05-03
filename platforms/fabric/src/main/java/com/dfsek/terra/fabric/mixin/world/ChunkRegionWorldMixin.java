package com.dfsek.terra.fabric.mixin.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.generator.FabricChunkGeneratorWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = World.class, prefix = "terra$"))
public abstract class ChunkRegionWorldMixin {
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
}
