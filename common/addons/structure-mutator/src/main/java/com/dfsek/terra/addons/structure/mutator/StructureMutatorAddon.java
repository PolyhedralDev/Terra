package com.dfsek.terra.addons.structure.mutator;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


public class StructureMutatorAddon implements AddonInitializer {
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPreLoadEvent.class)
                .then(event -> {
                    event.getPack().registerConfigType(new MutatedStructureConfigType(addon), addon.key("MUTATED_STRUCTURE"), 499);
                })
                .failThrough();
    }
}
