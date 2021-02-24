package com.dfsek.terra.fabric.world.entity;

import com.dfsek.terra.api.platform.entity.EntityType;

public class FabricEntityType implements EntityType {
    private final net.minecraft.entity.EntityType<?> type;

    public FabricEntityType(net.minecraft.entity.EntityType<?> type) {
        this.type = type;
    }

    @Override
    public net.minecraft.entity.EntityType<?> getHandle() {
        return type;
    }
}
