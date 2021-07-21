package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.distributor.distributors.PointSetDistributor;
import com.dfsek.terra.addons.feature.distributor.util.Point;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Distributor;

import java.util.Set;

public class PointSetDistributorTemplate implements ObjectTemplate<Distributor> {
    @Value("points")
    private @Meta Set<@Meta Point> points;

    @Override
    public Distributor get() {
        return new PointSetDistributor(points);
    }
}
