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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = World.class, prefix = "vw$"))
public abstract class ChunkRegionWorldMixin {
    public int vw$getMaxHeight() {
        return ((ChunkRegion) (Object) this).getDimensionHeight();
    }

    public ChunkGenerator vw$getGenerator() {
        return (ChunkGenerator) ((ChunkRegion) (Object) this).toServerWorld().getChunkManager().getChunkGenerator();
    }

    public Chunk vw$getChunkAt(int x, int z) {
        return (Chunk) ((ChunkRegion) (Object) this).getChunk(x, z);
    }

    public Block vw$getBlockAt(int x, int y, int z) {
        return new FabricBlock(new BlockPos(x, y, z), ((ChunkRegion) (Object) this));
    }

    public Entity vw$spawnEntity(Location location, EntityType entityType) {
        throw new UnsupportedOperationException();
    }

    public int vw$getMinHeight() {
        return 0;
    }

    public Object vw$getHandle() {
        return this;
    }

    public boolean vw$isTerraWorld() {
        return vw$getGenerator() instanceof GeneratorWrapper;
    }

    public TerraChunkGenerator vw$getTerraGenerator() {
        return ((FabricChunkGeneratorWrapper) vw$getGenerator()).getHandle();
    }
}
