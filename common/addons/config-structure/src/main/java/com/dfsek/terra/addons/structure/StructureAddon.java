package com.dfsek.terra.addons.structure;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.structure.ConfiguredStructure;

@Addon("config-structure")
@Version("1.0.0")
@Author("Terra")
public class StructureAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
        main.applyLoader(ConfiguredStructure.class, (t, o, l) -> null);
    }

    public void onConfigLoad(ConfigPackPreLoadEvent event) {

    }
}
