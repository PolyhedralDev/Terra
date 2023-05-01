package com.dfsek.terra.addons.image.colorsampler;

@FunctionalInterface
public interface ColorSampler {
    
    /**
     * @param x World x coordinate
     * @param z World z coordinate
     * @return Integer representing a web color
     */
    Integer apply(int x, int z);
}
