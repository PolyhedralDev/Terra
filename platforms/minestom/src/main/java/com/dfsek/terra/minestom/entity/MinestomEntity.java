package com.dfsek.terra.minestom.entity;

import com.dfsek.seismic.type.vector.Vector3;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;

import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.minestom.MinestomAdapter;
import com.dfsek.terra.minestom.world.TerraMinestomWorld;


public class MinestomEntity implements com.dfsek.terra.api.entity.Entity {
    private final Entity delegate;
    private final TerraMinestomWorld world;

    public MinestomEntity(Entity delegate, TerraMinestomWorld world) {
        this.delegate = delegate;
        this.world = world;
    }

    public static MinestomEntity spawn(double x, double y, double z, EntityType type, TerraMinestomWorld world) {
        Instance instance = world.getHandle();
        MinestomEntityType entityType = (MinestomEntityType) type;
        Entity entity = world.getEntityFactory().createEntity(entityType.getHandle(), entityType.getData());
        entity.setInstance(instance, new Pos(x, y, z));
        return new MinestomEntity(entity, world);
    }

    @Override
    public Vector3 position() {
        return MinestomAdapter.adapt(delegate.getPosition());
    }

    @Override
    public void position(Vector3 position) {
        delegate.teleport(MinestomAdapter.adapt(position));
    }

    @Override
    public void world(ServerWorld world) {
        delegate.setInstance(((TerraMinestomWorld) world).getHandle());
    }

    @Override
    public ServerWorld world() {
        return world;
    }

    @Override
    public Object getHandle() {
        return delegate;
    }
}
