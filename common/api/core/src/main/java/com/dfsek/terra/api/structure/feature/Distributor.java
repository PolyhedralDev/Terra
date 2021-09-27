package com.dfsek.terra.api.structure.feature;

public interface Distributor {
    boolean matches(int x, int z, long seed);
    
    default Distributor and(Distributor other) {
        return (x, z, seed) -> this.matches(x, z, seed) && other.matches(x, z, seed);
    }
    
    default Distributor or(Distributor other) {
        return (x, z, seed) -> this.matches(x, z, seed) || other.matches(x, z, seed);
    }
    
    static Distributor yes() {
        return (x, z, seed) -> true;
    }
    
    static Distributor no() {
        return (x, z, seed) -> false;
    }
}
