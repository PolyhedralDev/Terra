package com.dfsek.terra.bukkit.world.entity;

import com.dfsek.terra.api.data.ExtendedData;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.entity.EntityTypeExtended;


public final class BukkitEntityTypeExtended extends BukkitEntityType implements EntityTypeExtended {
    private final BukkitEntityData data;

    public BukkitEntityTypeExtended(org.bukkit.entity.EntityType delegate, String snbt) {
        super(delegate);
        this.data = new BukkitEntityData(snbt);
    }

    @Override
    public ExtendedData getData() {
        return data;
    }

    @Override
    public EntityTypeExtended setData(ExtendedData data) {
        Object handle = data.getHandle();
        if(!(handle instanceof String snbt)) {
            throw new IllegalArgumentException("Bukkit entity data must contain SNBT text");
        }
        return new BukkitEntityTypeExtended(getHandle(), snbt);
    }

    @Override
    public EntityType getType() {
        return new BukkitEntityType(getHandle());
    }
}
