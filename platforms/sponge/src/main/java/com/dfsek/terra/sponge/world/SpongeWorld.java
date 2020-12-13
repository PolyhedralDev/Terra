package com.dfsek.terra.sponge.world;

import com.dfsek.terra.api.generic.Entity;
import com.dfsek.terra.api.generic.Tree;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.sponge.world.generator.SpongeChunkGenerator;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

public class SpongeWorld implements World {
    private final org.spongepowered.api.world.World delegate;

    public SpongeWorld(org.spongepowered.api.world.World delegate) {
        this.delegate = delegate;
    }

    @Override
    public long getSeed() {
        return delegate.getProperties().getSeed();
    }

    @Override
    public int getMaxHeight() {
        return 255;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new SpongeChunkGenerator(delegate.getWorldGenerator());
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public UUID getUID() {
        return delegate.getUniqueId();
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return delegate.loadChunk(x, 0, z, false).isPresent(); // TODO: better implementation
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
        return null;
    }

    @Override
    public Block getBlockAt(Location l) {
        return null;
    }

    @Override
    public boolean generateTree(Location l, Tree vanillaTreeType) {
        return false;
    }

    @Override
    public void spawn(Location location, Class<Entity> entity, Consumer<Entity> consumer) {

    }

    @Override
    public Object getHandle() {
        return null;
    }
}
