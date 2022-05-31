package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.api.world.info.WorldProperties;

import net.minecraft.world.level.LevelHeightAccessor;


public class NMSWorldProperties implements WorldProperties {
    private final long seed;
    private final LevelHeightAccessor height;
    
    public NMSWorldProperties(long seed, LevelHeightAccessor height) {
        this.seed = seed;
        this.height = height;
    }
    
    @Override
    public Object getHandle() {
        return height;
    }
    
    @Override
    public long getSeed() {
        return seed;
    }
    
    @Override
    public int getMaxHeight() {
        return height.ag();
    }
    
    @Override
    public int getMinHeight() {
        return height.u_();
    }
}
