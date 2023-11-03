/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.numberpredicate;

import java.util.function.DoublePredicate;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


public class NumberPredicateAddon implements AddonInitializer {
    
    @Inject
    private Platform plugin;
    
    @Inject
    private BaseAddon addon;
    
    @Override
    public void initialize() {
        plugin.getEventManager()
              .getHandler(FunctionalEventHandler.class)
              .register(addon, ConfigPackPreLoadEvent.class)
              .then(event -> event.getPack().applyLoader(DoublePredicate.class, new DoublePredicateLoader()))
              .priority(50)
              .failThrough();
    }
}
