package com.dfsek.terra.bukkit;

import org.bukkit.Location;

import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitEntity implements Entity {
    private final org.bukkit.entity.Entity entity;
    
    public BukkitEntity(org.bukkit.entity.Entity entity) {
        this.entity = entity;
    }
    
    @Override
    public org.bukkit.entity.Entity getHandle() {
        return entity;
    }
    
    @Override
    public Vector3 position() {
        return BukkitAdapter.adapt(entity.getLocation().toVector());
    }
    
    @Override
    public void position(Vector3 location) {
        entity.teleport(BukkitAdapter.adapt(location).toLocation(entity.getWorld()));
    }
    
    @Override
    public void world(World world) {
        Location newLoc = entity.getLocation().clone();
        newLoc.setWorld(BukkitAdapter.adapt(world));
        entity.teleport(newLoc);
    }
    
    @Override
    public World world() {
        return BukkitAdapter.adapt(entity.getWorld());
    }
    
    @Override
    public void sendMessage(String message) {
        entity.sendMessage(message);
    }
}
