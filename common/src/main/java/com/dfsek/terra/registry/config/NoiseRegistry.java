package com.dfsek.terra.registry.config;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.math.noise.samplers.noise.NoiseFunction;
import com.dfsek.terra.registry.TerraRegistry;

import java.util.function.Supplier;

public class NoiseRegistry extends TerraRegistry<Supplier<ObjectTemplate<NoiseFunction>>> {

}
