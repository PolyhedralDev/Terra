package com.dfsek.terra.addons.flora;

import com.dfsek.terra.addons.flora.config.BlockLayerTemplate;
import com.dfsek.terra.addons.flora.flora.gen.BlockLayer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


@Addon("config-flora")
@Author("Terra")
@Version("0.1.0")
public class FloraAddon extends TerraAddon {
    @Inject
    private Platform platform;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> {
                event.getPack().registerConfigType(new FloraConfigType(), "FLORA", 2);
                event.getPack().applyLoader(BlockLayer.class, BlockLayerTemplate::new);
            })
                .failThrough();
    }
}
