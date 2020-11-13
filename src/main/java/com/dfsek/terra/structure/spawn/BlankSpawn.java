package com.dfsek.terra.structure.spawn;

public class BlankSpawn extends Requirement {
    public BlankSpawn() {
        super(null);
    }

    @Override
    public boolean matches(int x, int y, int z) {
        return true;
    }
}
