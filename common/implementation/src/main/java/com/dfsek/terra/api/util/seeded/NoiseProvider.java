package com.dfsek.terra.api.util.seeded;

import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.function.Supplier;

@FunctionalInterface
public interface NoiseProvider extends Supplier<ObjectTemplate<NoiseSeeded>> {
}
