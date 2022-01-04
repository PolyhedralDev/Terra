package com.dfsek.terra.addons.structure.mutator;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.inject.annotations.Inject;


public class StructureMutatorAddon implements AddonInitializer {
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    
    @Override
    public void initialize() {
    
    }
}
