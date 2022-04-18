package com.dfsek.terra.addons.palette.shortcut.block;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.structure.Structure;


public class StructureBlockShortcutAddon implements AddonInitializer {
    @Inject
    private BaseAddon addon;
    @Inject
    private Platform platform;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPreLoadEvent.class)
                .then(event -> event.getPack()
                                    .registerShortcut(Structure.class, "BLOCK",
                                                      (configLoader, input, tracker) -> new SingletonStructure(
                                                              configLoader.loadType(BlockState.class, input, tracker)
                                                      )))
                .failThrough();
    }
}
