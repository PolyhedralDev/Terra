/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.locators;

import java.util.Random;

import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.chunk.generation.util.Column;


public class RandomLocator implements Locator {
    private final Range height;
    
    private final Range points;
    
    public RandomLocator(Range height, Range points) {
        this.height = height;
        this.points = points;
    }
    
    @Override
    public BinaryColumn getSuitableCoordinates(Column<?> column) {
        long seed = column.getWorld().getSeed();
        seed = 31 * seed + column.getX();
        seed = 31 * seed + column.getZ();
        
        Random r = new Random(seed);
        
        int size = points.get(r);
        
        BinaryColumn results = new BinaryColumn(column.getMinY(), column.getMaxY());
        for(int i = 0; i < size; i++) {
            results.set(height.get(r));
        }
        
        return results;
    }
}
