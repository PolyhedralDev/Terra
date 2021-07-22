package com.dfsek.terra.addons.biome;

import com.dfsek.terra.addons.biome.holder.PaletteHolder;
import com.dfsek.terra.addons.biome.holder.PaletteHolderLoader;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;

@Addon("config-biome")
@Author("Terra")
@Version("1.0.0")
public class BiomeAddon extends TerraAddon {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager()
                .register(ConfigPackPreLoadEvent.class)
                .then(event -> {
                    event.getPack().registerConfigType(new BiomeConfigType(event.getPack()), "BIOME", 5);
                    event.getPack().applyLoader(PaletteHolder.class, new PaletteHolderLoader());
                });
    }
}
