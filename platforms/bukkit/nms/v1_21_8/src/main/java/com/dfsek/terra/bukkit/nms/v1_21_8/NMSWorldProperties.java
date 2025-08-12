package com.dfsek.terra.bukkit.nms.v1_21_8;

import net.minecraft.world.level.LevelHeightAccessor;

import com.dfsek.terra.api.world.info.WorldProperties;


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
        return height.getMaxY();
    }

    @Override
    public int getMinHeight() {
        return height.getMinY();
    }
}
