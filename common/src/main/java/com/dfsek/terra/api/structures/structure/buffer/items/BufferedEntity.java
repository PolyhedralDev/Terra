package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.event.events.world.generation.EntitySpawnEvent;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;

public class BufferedEntity implements BufferedItem {

    private final EntityType type;
    private final TerraPlugin main;

    public BufferedEntity(EntityType type, TerraPlugin main) {
        this.type = type;
        this.main = main;
    }

    @Override
    public void paste(Location origin) {
        Entity entity = origin.clone().add(0.5, 0, 0.5).getWorld().spawnEntity(origin, type);
        main.getEventManager().callEvent(new EntitySpawnEvent(main.getWorld(entity.getWorld()).getGenerator().getConfigPack(), entity, entity.getLocation()));
    }
}
