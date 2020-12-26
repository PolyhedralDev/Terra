package com.dfsek.terra.registry;

import com.dfsek.terra.generation.items.TerraStructure;

public class StructureRegistry extends TerraRegistry<TerraStructure> {
    @Override
    public boolean add(String name, TerraStructure value) {
        System.out.println("added structure " + name + " to registry");
        return super.add(name, value);
    }
}
