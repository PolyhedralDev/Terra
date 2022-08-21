/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome;

import com.dfsek.terra.addons.biome.holder.PaletteHolder;
import com.dfsek.terra.addons.biome.holder.PaletteHolderLoader;
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


public class BiomeAddon implements MonadAddonInitializer {
    @Override
    public @NotNull Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((functionalEventHandler, base, platform) -> Init.ofPure(
                        functionalEventHandler.register(base, ConfigPackPreLoadEvent.class)
                                              .then(event -> {
                                                  event.getPack().registerConfigType(new BiomeConfigType(event.getPack()),
                                                                                     base.key("BIOME"), 1000);
                                                  event.getPack().applyLoader(PaletteHolder.class, new PaletteHolderLoader());
                                              })
                                              .failThrough()
                                                                        )));
    }
}
