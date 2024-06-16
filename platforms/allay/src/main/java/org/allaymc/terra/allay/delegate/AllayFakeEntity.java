package org.allaymc.terra.allay.delegate;

import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public final class AllayFakeEntity implements Entity {

    private final Object fakeHandle = new Object();
    private Vector3 position;
    private ServerWorld world;

    public AllayFakeEntity(Vector3 position, ServerWorld world) {
        this.position = position;
        this.world = world;
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
    public void world(ServerWorld world) {
        this.world = world;
    }

    @Override
    public ServerWorld world() {
        return world;
    }

    @Override
    public Object getHandle() {
        return null;
    }
}
