package com.dfsek.terra.api.util.provider;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.structure.feature.Locator;

import java.util.function.Supplier;

public interface LocatorProvider extends Supplier<ObjectTemplate<Locator>> {
}
