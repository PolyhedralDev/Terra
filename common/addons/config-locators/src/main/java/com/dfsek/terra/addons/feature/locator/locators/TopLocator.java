/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.locators;

import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.chunk.generation.util.Column;


public class TopLocator implements Locator {
    private final Range search;
    
    public TopLocator(Range search) {
        this.search = search;
    }
    
    @Override
    public BinaryColumn getSuitableCoordinates(Column<?> column) {
        for(int y : search) {
            if(column.getBlock(y).isAir() && !column.getBlock(y - 1).isAir()) {
                return new BinaryColumn(y, y + 1, yi -> true);
            }
        }
        return BinaryColumn.getNull();
    }
}
