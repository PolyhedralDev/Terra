package com.dfsek.terra.addons.generation.flora;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.injection.annotations.Inject;

@Addon("generation-stage-flora")
@Version("1.0.0")
@Author("Terra")
public class FloraGenerationAddon extends TerraAddon implements EventListener {

    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }
}
