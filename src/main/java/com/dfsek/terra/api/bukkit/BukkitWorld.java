package com.dfsek.terra.api.bukkit;

import com.dfsek.terra.api.generic.world.World;

public class BukkitWorld implements World {
    private final org.bukkit.World delegate;

    public BukkitWorld(org.bukkit.World delegate) {
        this.delegate = delegate;
    }

    @Override
    public long getSeed() {
        return delegate.getSeed();
    }
}
