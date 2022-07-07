/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.locators;

import java.util.List;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.world.chunk.generation.util.Column;
import com.dfsek.terra.api.world.chunk.generation.util.Column.BinaryColumnBuilder;


public class SamplerLocator implements Locator {
    private final List<NoiseSampler> samplers;
    
    public SamplerLocator(List<NoiseSampler> samplers) {
        this.samplers = samplers;
    }
    
    private static int floorToInt(double value) {
        int valueInt = (int) value;
        if(value < 0.0) {
            if(value == (double) valueInt) {
                return valueInt;
            } else {
                return valueInt == Integer.MIN_VALUE ? valueInt : valueInt - 1;
            }
        } else {
            return valueInt;
        }
    }
    
    @Override
    public BinaryColumn getSuitableCoordinates(Column<?> column) {
        BinaryColumnBuilder results = column.newBinaryColumn();
        
        long seed = column.getWorld().getSeed();
        samplers.forEach(sampler -> {
            int y = floorToInt(sampler.noise(seed, column.getX(), column.getZ()));
            if(y >= column.getMaxY() || y < column.getMinY()) return;
            results.set(y);
        });
        
        return results.build();
    }
}
