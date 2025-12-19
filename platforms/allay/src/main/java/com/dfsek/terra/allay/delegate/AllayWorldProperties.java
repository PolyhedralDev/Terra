package com.dfsek.terra.allay.delegate;

import org.allaymc.api.world.data.DimensionInfo;

import com.dfsek.terra.api.world.info.WorldProperties;


/**
 * @author daoge_cmd
 */
public class AllayWorldProperties implements WorldProperties {

    private final Object fakeHandle;
    private final long seed;
    private final DimensionInfo dimensionInfo;

    public AllayWorldProperties(long seed, DimensionInfo dimensionInfo) {
        this.fakeHandle = new Object();
        this.seed = seed;
        this.dimensionInfo = dimensionInfo;
    }

    @Override
    public long getSeed() {
        return this.seed;
    }

    @Override
    public int getMaxHeight() {
        return dimensionInfo.maxHeight();
    }

    @Override
    public int getMinHeight() {
        return dimensionInfo.minHeight();
    }

    @Override
    public Object getHandle() {
        return fakeHandle;
    }
}
