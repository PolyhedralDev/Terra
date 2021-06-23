package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.event.events.world.generation.EntitySpawnEvent;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;

public class BufferedEntity implements BufferedItem {

    private final EntityType type;
    private final TerraPlugin main;

    public BufferedEntity(EntityType type, TerraPlugin main) {
        this.type = type;
        this.main = main;
    }

    @Override
    public void paste(LocationImpl origin) {
        Entity entity = origin.clone().add(0.5, 0, 0.5).getWorld().spawnEntity(origin, type);
        main.getEventManager().callEvent(new EntitySpawnEvent(entity.getWorld().getTerraGenerator().getConfigPack(), entity, entity.getLocation()));
    }
}
