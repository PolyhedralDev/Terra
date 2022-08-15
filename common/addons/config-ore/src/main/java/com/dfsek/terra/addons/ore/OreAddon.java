/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.function.monad.Monad;


public class OreAddon implements MonadAddonInitializer {
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(manager -> manager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                ((eventHandler, addon) -> Init
                        .ofPure(eventHandler
                                        .register(addon, ConfigPackPreLoadEvent.class)
                                        .then(event -> event
                                                .getPack()
                                                .registerConfigType(new OreConfigType(), addon.key("ORE"), 1))
                                        .failThrough()))
                      );
    }
}
