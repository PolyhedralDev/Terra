package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.fabric.world.handles.FabricWorldAccess;
import net.minecraft.block.Block;
import net.minecraft.world.WorldAccess;

public class BlockStorage {
    private final Block block;
    private final Location location;

    public BlockStorage(Block block, Location location) {
        this.block = block;
        this.location = location;
    }

    public Block getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }

    public WorldAccess getWorld() {
        return ((FabricWorldAccess) location.getWorld()).getHandle();
    }


}
