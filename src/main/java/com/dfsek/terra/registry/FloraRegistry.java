package com.dfsek.terra.registry;

import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.FloraType;

public class FloraRegistry extends TerraRegistry<Flora> {
    public FloraRegistry() {
        for(FloraType f : FloraType.values()) add(f.toString(), f);
    }
}
