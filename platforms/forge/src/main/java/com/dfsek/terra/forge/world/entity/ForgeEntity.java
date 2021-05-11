package com.dfsek.terra.forge.world.entity;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.forge.world.ForgeAdapter;
import com.dfsek.terra.forge.world.handles.world.ForgeWorldAccess;
import com.dfsek.terra.forge.world.handles.world.ForgeWorldHandle;
import net.minecraft.world.server.ServerWorld;

public class ForgeEntity implements Entity {
    private final net.minecraft.entity.Entity delegate;

    public ForgeEntity(net.minecraft.entity.Entity delegate) {
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
        return new Location(new ForgeWorldAccess(delegate.level), ForgeAdapter.adapt(delegate.blockPosition()));
    }

    @Override
    public void setLocation(Location location) {
        delegate.teleportTo(location.getX(), location.getY(), location.getZ());
        delegate.setLevel((ServerWorld) ((ForgeWorldHandle) location).getWorld());
    }

    @Override
    public World getWorld() {
        return new ForgeWorldAccess(delegate.level);
    }
}
