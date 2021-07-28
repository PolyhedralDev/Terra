package com.dfsek.terra.addons.feature.locator.locators;

import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.Column;

import java.util.ArrayList;
import java.util.List;

public class PatternLocator implements Locator {
    private final Pattern pattern;
    private final Range search;

    public PatternLocator(Pattern pattern, Range search) {
        this.pattern = pattern;
        this.search = search;
    }

    @Override
    public List<Integer> getSuitableCoordinates(Column column) {
        List<Integer> locations = new ArrayList<>();

        for(int y : search) {
            if(pattern.matches(y, column)) locations.add(y);
        }

        return locations;
    }
}
