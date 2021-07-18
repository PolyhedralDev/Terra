package com.dfsek.terra.api.structure.feature;

public interface Distributor {
    boolean matches(int x, int z);

    default Distributor and(Distributor other) {
        return (x, z) -> this.matches(x, z) && other.matches(x, z);
    }

    default Distributor or(Distributor other) {
        return (x, z) -> this.matches(x, z) || other.matches(x, z);
    }
}
