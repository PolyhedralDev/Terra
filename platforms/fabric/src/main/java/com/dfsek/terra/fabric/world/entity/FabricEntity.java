package com.dfsek.terra.fabric.world.entity;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.fabric.world.FabricAdapter;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldAccess;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldHandle;
import net.minecraft.server.world.ServerWorld;

public class FabricEntity implements Entity {
    private final net.minecraft.entity.Entity delegate;

    public FabricEntity(net.minecraft.entity.Entity delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public Object getHandle() {
        return null;
    }

    @Override
    public Location getLocation() {
        return new Location(new FabricWorldAccess(delegate.world), FabricAdapter.adapt(delegate.getBlockPos()));
    }

    @Override
    public void setLocation(Location location) {
        delegate.teleport(location.getX(), location.getY(), location.getZ());
        delegate.moveToWorld((ServerWorld) ((FabricWorldHandle) location).getWorld());
    }

    @Override
    public World getWorld() {
        return new FabricWorldAccess(delegate.world);
    }
}
