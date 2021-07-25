package com.dfsek.terra;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;

@Addon("terra")
@Author("Terra")
@Version("1.0.0")
public class InternalAddon extends TerraAddon {
    private final AbstractTerraPlugin main;

    public InternalAddon(AbstractTerraPlugin main) {
        this.main = main;
    }

    @Override
    public void initialize() {
        main.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, PlatformInitializationEvent.class)
                .then(event -> {
                    main.logger().info("Loading config packs...");
                    main.getRawConfigRegistry().loadAll(main);
                    main.logger().info("Loaded packs.");
                })
                .global();
    }
}
