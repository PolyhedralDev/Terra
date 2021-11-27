/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.locators;

import net.jafama.FastMath;

import java.util.List;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.world.Column;


public class NoiseLocator implements Locator {
    private final List<NoiseSampler> samplers;
    
    public NoiseLocator(List<NoiseSampler> samplers) {
        this.samplers = samplers;
    }
    
    @Override
    public BinaryColumn getSuitableCoordinates(Column column) {
        BinaryColumn results = new BinaryColumn(column.getMinY(), column.getMaxY());
        
        long seed = column.getWorld().getSeed();
        samplers.forEach(sampler -> {
            int y = FastMath.floorToInt(sampler.noise(seed, column.getX(), column.getX()));
            if(y >= column.getMaxY() || y < column.getMinY()) return;
            results.set(y);
        });
        
        return results;
    }
}
