package com.dfsek.terra.addons.terrascript.buffer.items;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.event.events.world.generation.EntitySpawnEvent;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;

public class BufferedEntity implements BufferedItem {

    private final EntityType type;
    private final TerraPlugin main;

    public BufferedEntity(EntityType type, TerraPlugin main) {
        this.type = type;
        this.main = main;
    }

    @Override
    public void paste(Vector3 origin, World world) {
        Entity entity = world.spawnEntity(origin.clone().add(0.5, 0, 0.5), type);
        main.getEventManager().callEvent(new EntitySpawnEvent(entity.world().getGenerator().getConfigPack(), entity));
    }
}
