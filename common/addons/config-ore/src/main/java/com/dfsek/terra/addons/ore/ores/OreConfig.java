/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore.ores;

import com.dfsek.terra.api.util.Range;


public class OreConfig {
    private final Range amount;
    private final Range height;
    
    public OreConfig(Range amount, Range height) {
        this.amount = amount;
        this.height = height;
    }
    
    public Range getAmount() {
        return amount;
    }
    
    public Range getHeight() {
        return height;
    }
}
