package com.dfsek.terra.mod.implmentation;

import net.minecraft.nbt.CompoundTag;

import com.dfsek.terra.api.data.ExtendedData;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.entity.EntityTypeExtended;


public record MinecraftEntityTypeExtended(net.minecraft.world.entity.EntityType<?> entityType, CompoundTag nbtCompound)
    implements EntityTypeExtended {
    @SuppressWarnings("DataFlowIssue")
    @Override
    public ExtendedData getData() {
        return (ExtendedData) ((Object) nbtCompound);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public EntityTypeExtended setData(ExtendedData data) {
        return new MinecraftEntityTypeExtended(entityType,
            data.getClass().equals(CompoundTag.class) ? ((CompoundTag) ((Object) data)) : null);
    }

    @Override
    public EntityType getType() {
        return (EntityType) entityType;
    }

    @Override
    public Object getHandle() {
        return entityType;
    }
}
