package com.dfsek.terra.registry;

import com.dfsek.terra.api.gaea.world.Flora;
import com.dfsek.terra.api.gaea.world.FloraType;

public class FloraRegistry extends TerraRegistry<Flora> {
    public FloraRegistry() {
        for(FloraType f : FloraType.values()) add(f.toString(), f);
    }

    @Override
    public Flora get(String id) {
        return super.get(id);
    }
}
