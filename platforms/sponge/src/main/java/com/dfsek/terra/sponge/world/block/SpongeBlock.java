package com.dfsek.terra.sponge.world.block;

import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.sponge.world.SpongeWorld;
import org.spongepowered.api.world.World;

public class SpongeBlock implements Block {
    private final org.spongepowered.api.world.Location<World> delegate;

    public SpongeBlock(org.spongepowered.api.world.Location<World> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        delegate.setBlock(((SpongeBlockData) data).getHandle());
    }

    @Override
    public BlockData getBlockData() {
        return new SpongeBlockData(delegate.getBlock());
    }

    @Override
    public Block getRelative(BlockFace face) {
        return new SpongeBlock(delegate.copy().add(face.getModX(), face.getModY(), face.getModZ()));
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        return new SpongeBlock(delegate.copy().add(face.getModX() * len, face.getModY() * len, face.getModZ() * len));
    }

    @Override
    public boolean isEmpty() {
        return delegate.hasBlock();
    }

    @Override
    public Location getLocation() {
        return new Location(new SpongeWorld(delegate.getExtent()), delegate.getX(), delegate.getY(), delegate.getZ());
    }

    @Override
    public MaterialData getType() {
        return new SpongeMaterialData(delegate.getBlockType());
    }

    @Override
    public int getX() {
        return delegate.getBlockX();
    }

    @Override
    public int getZ() {
        return delegate.getBlockZ();
    }

    @Override
    public int getY() {
        return delegate.getBlockY();
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public org.spongepowered.api.world.Location<World> getHandle() {
        return delegate;
    }
}
