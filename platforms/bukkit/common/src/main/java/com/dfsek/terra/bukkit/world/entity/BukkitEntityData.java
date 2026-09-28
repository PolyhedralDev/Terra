package com.dfsek.terra.bukkit.world.entity;

import com.dfsek.terra.api.data.ExtendedData;


public record BukkitEntityData(String snbt) implements ExtendedData {
    @Override
    public Object getHandle() {
        return snbt;
    }

    @Override
    public String toString() {
        return snbt;
    }
}
