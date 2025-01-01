package com.dfsek.terra.minestom.entity;

import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.minestom.world.TerraMinestomWorld;


public class DeferredMinestomEntity implements Entity {
    private final EntityType type;
    private double x;
    private double y;
    private double z;
    private TerraMinestomWorld world;

    public DeferredMinestomEntity(double x, double y, double z, EntityType type, TerraMinestomWorld world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.world = world;
    }

    @Override
    public Vector3 position() {
        return Vector3.of(x, y, z);
    }

    @Override
    public void position(Vector3 position) {
        x = position.getX();
        y = position.getY();
        z = position.getZ();
    }

    @Override
    public void world(ServerWorld world) {
        this.world = (TerraMinestomWorld) world;
    }

    @Override
    public ServerWorld world() {
        return world;
    }

    @Override
    public Object getHandle() {
        return this;
    }

    public void spawn() {
        int chunkX = (int) x >> 4;
        int chunkZ = (int) z >> 4;

        if(!world.getHandle().isChunkLoaded(chunkX, chunkZ)) {
            return;
        }

        MinestomEntity.spawn(x, y, z, type, world);
    }
}
