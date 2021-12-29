package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.distributor.distributors.PaddedGridDistributor;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Distributor;


public class PaddedGridDistributorTemplate implements ObjectTemplate<Distributor> {
    @Value("width")
    private @Meta int width;
    
    @Value("padding")
    private @Meta int padding;
    
    @Value("salt")
    private @Meta int salt;
    
    @Override
    public Distributor get() {
        return new PaddedGridDistributor(width, padding, salt);
    }
}
