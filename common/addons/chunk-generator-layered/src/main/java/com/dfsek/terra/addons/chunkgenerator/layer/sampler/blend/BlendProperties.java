package com.dfsek.terra.addons.chunkgenerator.layer.sampler.blend;

public record BlendProperties(int density, double weight, int extent) {
    public static BlendProperties of(int density, double weight) {
        return new BlendProperties(density, weight, Math.max((18 / density) + 1, 1));
    }
}
