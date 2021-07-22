package com.dfsek.terra.addons.palette;

import com.dfsek.terra.addons.palette.palette.PaletteLayerHolder;
import com.dfsek.terra.addons.palette.palette.PaletteLayerLoader;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.injection.annotations.Inject;

@Addon("config-palette")
@Author("Terra")
@Version("1.0.0")
public class PaletteAddon extends TerraAddon {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> {
                    event.getPack().registerConfigType(new PaletteConfigType(main), "PALETTE", 2);
                    event.getPack().applyLoader(PaletteLayerHolder.class, new PaletteLayerLoader());
                })
                .failThrough();
    }
}
