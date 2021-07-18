package com.dfsek.terra.api.util.provider;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

import java.util.function.Supplier;

@FunctionalInterface
public interface NoiseProvider extends Supplier<ObjectTemplate<SeededNoiseSampler>> {
}
