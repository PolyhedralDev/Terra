package com.dfsek.terra.addons.structure.mutator;

import com.dfsek.tectonic.api.exception.LoadException;

import com.dfsek.terra.addons.structure.mutator.config.MutatedStructureTemplate;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.ConfigFactory;


public class MutatedStructureFactory implements ConfigFactory<MutatedStructureTemplate, MutatedStructure> {
    private final BaseAddon addon;
    
    public MutatedStructureFactory(BaseAddon addon) {
        this.addon = addon;
    }
    
    @Override
    public MutatedStructure build(MutatedStructureTemplate config, Platform platform) throws LoadException {
        return new MutatedStructure(addon.key(config.getID()),
                                    config.getDelegate(),
                                    config.getReadInterceptor(),
                                    config.getWriteInterceptor());
    }
}
