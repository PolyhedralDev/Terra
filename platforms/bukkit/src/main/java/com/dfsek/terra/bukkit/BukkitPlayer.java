package com.dfsek.terra.bukkit;

import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

public class BukkitPlayer implements Player {
    private final org.bukkit.entity.Player delegate;

    public BukkitPlayer(org.bukkit.entity.Player delegate) {
        this.delegate = delegate;
    }

    @Override
    public org.bukkit.entity.Player getHandle() {
        return delegate;
    }

    @Override
    public LocationImpl getLocation() {
        org.bukkit.Location bukkit = delegate.getLocation();
        return new LocationImpl(BukkitAdapter.adapt(bukkit.getWorld()), bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }

    @Override
    public void setLocation(LocationImpl location) {
        delegate.teleport(BukkitAdapter.adapt(location));
    }

    @Override
    public World getWorld() {
        return BukkitAdapter.adapt(delegate.getWorld());
    }

    @Override
    public void sendMessage(String message) {
        delegate.sendMessage(message);
    }
}
