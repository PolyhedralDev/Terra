package com.dfsek.terra.addons.feature.locator.locators;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.Column;


public class SurfaceLocator implements Locator {
    private final Range search;
    
    private final BlockState air;
    
    public SurfaceLocator(Range search, Platform main) {
        this.search = search;
        this.air = main.getWorldHandle().air();
    }
    
    @Override
    public BinaryColumn getSuitableCoordinates(Column column) {
        BinaryColumn location = new BinaryColumn(column.getMinY(), column.getMaxY());
        for(int y : search) {
            if(column.getBlock(y).matches(air) && !column.getBlock(y - 1).matches(air)) {
                location.set(y);
                return location;
            }
        }
        return location;
    }
}
