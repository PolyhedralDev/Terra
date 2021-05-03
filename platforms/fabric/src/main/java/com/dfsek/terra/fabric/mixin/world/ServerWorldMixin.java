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
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerWorld.class)
@Implements(@Interface(iface = World.class, prefix = "vw$"))
public abstract class ServerWorldMixin {
    public int vw$getMaxHeight() {
        return ((ServerWorld) (Object) this).getDimensionHeight();
    }

    public ChunkGenerator vw$getGenerator() {
        return (ChunkGenerator) ((ServerWorld) (Object) this).getChunkManager().getChunkGenerator();
    }

    public Chunk vw$getChunkAt(int x, int z) {
        return (Chunk) ((ServerWorld) (Object) this).getChunk(x, z);
    }

    public Block vw$getBlockAt(int x, int y, int z) {
        return new FabricBlock(new BlockPos(x, y, z), ((ServerWorld) (Object) this));
    }

    public Entity vw$spawnEntity(Location location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(((ServerWorld) (Object) this));
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((ServerWorld) (Object) this).spawnEntity(entity);
        return (Entity) entity;
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
