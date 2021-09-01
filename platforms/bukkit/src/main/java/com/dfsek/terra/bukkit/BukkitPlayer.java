package com.dfsek.terra.bukkit;

import org.bukkit.Location;

import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.vector.Vector3;
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
    public Vector3 position() {
        org.bukkit.Location bukkit = delegate.getLocation();
        return new Vector3(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
    
    @Override
    public void position(Vector3 location) {
        delegate.teleport(BukkitAdapter.adapt(location).toLocation(delegate.getWorld()));
    }
    
    @Override
    public void world(World world) {
        Location newLoc = delegate.getLocation().clone();
        newLoc.setWorld(BukkitAdapter.adapt(world));
        delegate.teleport(newLoc);
    }
    
    @Override
    public World world() {
        return BukkitAdapter.adapt(delegate.getWorld());
    }
    
    @Override
    public void sendMessage(String message) {
        delegate.sendMessage(message);
    }
}
