package com.dfsek.terra.fabric.world.block;

import com.dfsek.terra.api.platform.world.block.Block;
import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.platform.world.block.BlockFace;
import com.dfsek.terra.api.platform.world.block.MaterialData;
import com.dfsek.terra.api.platform.world.vector.Location;
import com.dfsek.terra.fabric.world.FabricAdapters;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class FabricBlock implements Block {
    private final Handle delegate;

    public FabricBlock(BlockPos position, WorldAccess worldAccess) {
        this.delegate = new Handle(position, worldAccess);
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        delegate.worldAccess.setBlockState(delegate.position, ((FabricBlockData) data).getHandle(), 0, 0);
    }

    @Override
    public BlockData getBlockData() {
        return new FabricBlockData(delegate.worldAccess.getBlockState(delegate.position));
    }

    @Override
    public Block getRelative(BlockFace face) {
        return getRelative(face, 1);
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        BlockPos newPos = delegate.position.add(face.getModX() * len, face.getModY() * len, face.getModZ() * len);
        return new FabricBlock(newPos, delegate.worldAccess);
    }

    @Override
    public boolean isEmpty() {
        return getBlockData().getMaterial().isAir();
    }

    @Override
    public Location getLocation() {
        return FabricAdapters.toVector(delegate.position).toLocation(new FabricWorldAccess(delegate.worldAccess));
    }

    @Override
    public MaterialData getType() {
        return getBlockData().getMaterial();
    }

    @Override
    public int getX() {
        return delegate.position.getX();
    }

    @Override
    public int getZ() {
        return delegate.position.getZ();
    }

    @Override
    public int getY() {
        return delegate.position.getY();
    }

    @Override
    public boolean isPassable() {
        return isEmpty();
    }

    @Override
    public Handle getHandle() {
        return delegate;
    }

    public static final class Handle {
        private final BlockPos position;
        private final WorldAccess worldAccess;

        public Handle(BlockPos position, WorldAccess worldAccess) {
            this.position = position;
            this.worldAccess = worldAccess;
        }
    }
}
