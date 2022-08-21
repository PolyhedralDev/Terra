/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.util.function.monad.Monad;

import org.jetbrains.annotations.NotNull;


public class FeatureAddon implements MonadAddonInitializer {
    @Override
    public @NotNull Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                ((functionalEventHandler, base) -> Init.ofPure(
                        functionalEventHandler.register(base, ConfigPackPreLoadEvent.class)
                                              .then(event -> event.getPack()
                                                                  .registerConfigType(new FeatureConfigType(), base.key("FEATURE"), 500))
                                              .failThrough()))
                      );
        
    }
}
