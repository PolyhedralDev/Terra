/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.palette;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.addons.palette.palette.PaletteLayerHolder;
import com.dfsek.terra.addons.palette.palette.PaletteLayerLoader;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.function.monad.Monad;


public class PaletteAddon implements MonadAddonInitializer {
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((handler, base, platform) -> Init.ofPure(
                        handler.register(base, ConfigPackPreLoadEvent.class)
                               .then(event -> {
                                   event.getPack().registerConfigType(new PaletteConfigType(platform), base.key("PALETTE"), 2);
                                   event.getPack().applyLoader(PaletteLayerHolder.class, PaletteLayerLoader::new);
                               })
                               .failThrough()))
                      );
    }
}
