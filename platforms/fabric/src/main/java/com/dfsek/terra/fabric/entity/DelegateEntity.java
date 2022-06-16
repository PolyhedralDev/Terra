package com.dfsek.terra.fabric.entity;

import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;

import net.minecraft.world.ServerWorldAccess;


public class DelegateEntity implements Entity {
    private Vector3 position;
    private ServerWorld world;
    private final EntityType type;
    
    public DelegateEntity(Vector3 position, ServerWorld world, EntityType type) {
        this.world = world;
        this.position = position;
        this.type = type;
    }
    
    @Override
    public Vector3 position() {
        return position;
    }
    
    @Override
    public void position(Vector3 position) {
        this.position = position;
    }
    
    @Override
    public ServerWorld world() {
        return world;
    }
    
    @Override
    public void world(ServerWorld world) {
        this.world = world;
    }
    
    @Override
    public Object getHandle() {
        return this;
    }
    
    public net.minecraft.entity.Entity createMinecraftEntity(ServerWorldAccess world) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) type).create(world.toServerWorld());
        entity.setPos(position.getX(), position.getY(), position.getZ());
        return entity;
    }
}
