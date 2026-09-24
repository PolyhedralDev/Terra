package com.dfsek.terra.allay.delegate;

import org.allaymc.api.world.dimension.DimensionType;

import com.dfsek.terra.api.world.info.WorldProperties;


/**
 * @author daoge_cmd
 */
public class AllayWorldProperties implements WorldProperties {

    private final Object fakeHandle;
    private final long seed;
    private final DimensionType dimensionType;

    public AllayWorldProperties(long seed, DimensionType dimensionType) {
        this.fakeHandle = new Object();
        this.seed = seed;
        this.dimensionType = dimensionType;
    }

    @Override
    public long getSeed() {
        return this.seed;
    }

    @Override
    public int getMaxHeight() {
        return dimensionType.getMaxHeight();
    }

    @Override
    public int getMinHeight() {
        return dimensionType.getMinHeight();
    }

    @Override
    public Object getHandle() {
        return fakeHandle;
    }
}
