package com.dfsek.terra.api.structure.feature;

import com.dfsek.terra.api.structure.Structure;

public interface Feature {
    Structure getStructure(double x, double y, double z);

    Distributor getDistributor();

    Locator getLocator();
}
