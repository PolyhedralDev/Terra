package com.dfsek.terra.bukkit.world;

import org.bukkit.generator.WorldInfo;

import com.dfsek.terra.api.world.info.WorldProperties;


public class BukkitWorldProperties implements WorldProperties {
    private final WorldInfo delegate;
    
    public BukkitWorldProperties(WorldInfo delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public Object getHandle() {
        return delegate;
    }
    
    @Override
    public long getSeed() {
        return delegate.getSeed();
    }
    
    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }
    
    @Override
    public int getMinHeight() {
        return delegate.getMinHeight();
    }
}
