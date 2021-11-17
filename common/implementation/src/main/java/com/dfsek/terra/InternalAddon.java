package com.dfsek.terra;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;


public class InternalAddon implements BaseAddon {
    @Override
    public String getID() {
        return "terra";
    }
}
