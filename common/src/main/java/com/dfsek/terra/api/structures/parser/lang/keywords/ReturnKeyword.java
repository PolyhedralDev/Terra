package com.dfsek.terra.api.structures.parser.lang.keywords;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Keyword;

public class ReturnKeyword implements Keyword<Void> {
    @Override
    public Void apply(Location location) {
        return null;
    }

    @Override
    public Void apply(Location location, Chunk chunk) {
        return null;
    }
}
