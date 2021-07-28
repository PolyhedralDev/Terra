package com.dfsek.terra.addons.feature.locator.locators;

import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLocator implements Locator {
    private final Range height;

    private final Range points;

    public RandomLocator(Range height, Range points) {
        this.height = height;
        this.points = points;
    }

    @Override
    public BinaryColumn getSuitableCoordinates(Column column) {
        long seed = column.getWorld().getSeed();
        seed = 31 * seed + column.getX();
        seed = 31 * seed + column.getZ();

        Random r = new Random(seed);

        int size = points.get(r);

        List<Integer> results = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            results.set(i, height.get(r));
        }

        return results;
    }
}
