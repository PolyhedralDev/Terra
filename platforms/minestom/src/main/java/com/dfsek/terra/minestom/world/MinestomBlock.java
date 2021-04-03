package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.api.util.generic.either.Either;
import net.minestom.server.utils.BlockPosition;

public class MinestomBlock implements Block {
    private final net.minestom.server.instance.block.Block delegate;
    private final BlockPosition position;
    private final Either<MinestomChunkAccess, MinestomWorld> world;

    public MinestomBlock(net.minestom.server.instance.block.Block delegate, BlockPosition position, Either<MinestomChunkAccess, MinestomWorld> world) {
        this.delegate = delegate;
        this.position = position;
        this.world = world;
    }

    @Override
    public net.minestom.server.instance.block.Block getHandle() {
        return delegate;
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        world.ifLeft(chunk -> chunk.getHandle().setBlock(position.getX() - (chunk.getX() << 4), position.getY(), position.getZ() - (chunk.getZ() << 4), ((MinestomBlockData) data).getHandle()));
        world.ifRight(world -> {
            //world.getHandle().setBlock(position.getX(), position.getY(), position.getZ(), delegate);
        });
    }

    @Override
    public BlockData getBlockData() {
        return new MinestomBlockData(delegate);
    }

    @Override
    public BlockState getState() {
        return null;
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        BlockPosition newLoc = position.clone().add(face.getModX(), face.getModY(), face.getModZ());
        if(world.hasLeft()) return world.getLeft().get().getBlock(newLoc.getX(), newLoc.getY(), newLoc.getZ());
        else return world.getRight().get().getBlockAt(newLoc.getX(), newLoc.getY(), newLoc.getZ());
    }

    @Override
    public boolean isEmpty() {
        return delegate.isAir();
    }

    @Override
    public Location getLocation() {
        if(world.hasLeft()) return new Location(world.getLeft().get().getWorld(), position.getX(), position.getY(), position.getZ());
        return new Location(world.getRight().get(), position.getX(), position.getY(), position.getZ());
    }

    @Override
    public int getX() {
        return position.getX();
    }

    @Override
    public int getZ() {
        return position.getZ();
    }

    @Override
    public int getY() {
        return position.getY();
    }

    @Override
    public boolean isPassable() {
        return !delegate.isSolid();
    }
}
