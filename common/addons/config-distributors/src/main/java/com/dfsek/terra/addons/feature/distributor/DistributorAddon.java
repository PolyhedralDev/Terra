/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.distributor;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.feature.distributor.config.AndDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.NoDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.OrDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.PaddedGridDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.PointSetDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.SamplerDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.XorDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.YesDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.util.Point;
import com.dfsek.terra.addons.feature.distributor.util.PointTemplate;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class DistributorAddon implements AddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<Distributor>>> DISTRIBUTOR_TOKEN = new TypeKey<>() {
    };
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPreLoadEvent.class)
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<Distributor>>> distributorRegistry = event
                            .getPack()
                            .getOrCreateRegistry(DISTRIBUTOR_TOKEN);
            
                    distributorRegistry.register(addon.key("SAMPLER"), SamplerDistributorTemplate::new);
                    distributorRegistry.register(addon.key("POINTS"), PointSetDistributorTemplate::new);
                    distributorRegistry.register(addon.key("PADDED_GRID"), PaddedGridDistributorTemplate::new);
                    distributorRegistry.register(addon.key("AND"), AndDistributorTemplate::new);
                    distributorRegistry.register(addon.key("OR"), OrDistributorTemplate::new);
                    distributorRegistry.register(addon.key("XOR"), XorDistributorTemplate::new);
                    distributorRegistry.register(addon.key("YES"), YesDistributorTemplate::new);
                    distributorRegistry.register(addon.key("NO"), NoDistributorTemplate::new);
            
                    event.getPack()
                         .applyLoader(Point.class, PointTemplate::new);
                })
                .failThrough();
    }
}
