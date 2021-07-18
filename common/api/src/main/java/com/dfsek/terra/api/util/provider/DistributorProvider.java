package com.dfsek.terra.api.util.provider;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.structure.feature.Distributor;

import java.util.function.Supplier;

public interface DistributorProvider extends Supplier<ObjectTemplate<Distributor>> {
}
