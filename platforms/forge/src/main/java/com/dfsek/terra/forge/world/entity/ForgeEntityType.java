package com.dfsek.terra.forge.world.entity;

import com.dfsek.terra.api.platform.entity.EntityType;

public class ForgeEntityType implements EntityType {
    private final net.minecraft.entity.EntityType<?> type;

    public ForgeEntityType(net.minecraft.entity.EntityType<?> type) {
        this.type = type;
    }

    @Override
    public net.minecraft.entity.EntityType<?> getHandle() {
        return type;
    }
}
