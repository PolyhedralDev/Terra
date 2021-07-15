package com.dfsek.terra.addons.palette;

import com.dfsek.terra.addons.palette.palette.PaletteLayerHolder;
import com.dfsek.terra.addons.palette.palette.PaletteLayerLoader;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;

@Addon("config-palette")
@Author("Terra")
@Version("1.0.0")
public class PaletteAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }

    public void onPackLoad(ConfigPackPreLoadEvent event) {
        event.getPack().registerConfigType(new PaletteConfigType(event.getPack(), main), "PALETTE", 2);
        event.getPack().applyLoader(PaletteLayerHolder.class, new PaletteLayerLoader());
    }
}
