package com.dfsek.terra.fabric.world.handles.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.entity.Entity;
import com.dfsek.terra.api.platform.world.entity.EntityType;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.generator.FabricChunkGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;

import java.io.File;
import java.util.UUID;

public class FabricWorldAccess implements World {
    private final WorldAccess delegate;

    public FabricWorldAccess(WorldAccess delegate) {
        this.delegate = delegate;
    }

    @Override
    public long getSeed() {
        return ((StructureWorldAccess) delegate).getSeed();
    }

    @Override
    public int getMaxHeight() {
        return delegate.getDimensionHeight();
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new FabricChunkGenerator(((ServerWorldAccess) delegate).toServerWorld().getChunkManager().getChunkGenerator());
    }

    @Override
    public String getName() {
        return ((ServerWorldAccess) delegate).toServerWorld().worldProperties.getLevelName();
    }

    @Override
    public UUID getUID() {
        return null;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return null;
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return new FabricBlock(pos, delegate);
    }

    @Override
    public Block getBlockAt(Location l) {
        return getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    @Override
    public boolean generateTree(Location l, Tree vanillaTreeType) {
        return false;
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        return null;
    }

    @Override
    public WorldAccess getHandle() {
        return delegate;
    }
}
