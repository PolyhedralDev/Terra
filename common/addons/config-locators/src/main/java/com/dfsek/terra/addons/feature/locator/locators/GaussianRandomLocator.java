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
import com.dfsek.terra.api.world.chunk.generation.util.Column.BinaryColumnBuilder;


public class GaussianRandomLocator implements Locator {
    private final double mean;
    
    private final Range points;
    
    private final double standardDeviation;
    
    private final int salt;
    
    
    public GaussianRandomLocator(Range height, Range points, double standardDeviation, int salt) {
        this.mean = (height.getMax() + height.getMin()) / 2.0;
        this.points = points;
        this.standardDeviation = standardDeviation;
        this.salt = salt;
    }
    
    @Override
    public BinaryColumn getSuitableCoordinates(Column<?> column) {
        long seed = column.getWorld().getSeed();
        seed = 31 * seed + column.getX();
        seed = 31 * seed + column.getZ();
        seed += salt;
        
        Random r = new Random(seed);
        
        int size = points.get(r);
        
        
        BinaryColumnBuilder results = column.newBinaryColumn();
        for(int i = 0; i < size; i++) {
            int h = (int) r.nextGaussian(mean, standardDeviation);
            if(h >= column.getMaxY() || h < column.getMinY()) continue;
            results.set(h);
        }
        
        return results.build();
    }
}
