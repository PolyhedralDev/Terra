package com.dfsek.terra.addons.flora;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;

@Addon("core-flora-config")
@Author("Terra")
@Version("0.1.0")
public class FloraAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }

    public void onPackLoad(ConfigPackPreLoadEvent event) throws DuplicateEntryException {
        event.getPack().registerConfigType(new FloraConfigType(event.getPack()), "FLORA", 2);
        event.getPack().getOrCreateRegistry(GenerationStageProvider.class).register("FLORA", pack -> new FloraPopulator(main));
    }
}
