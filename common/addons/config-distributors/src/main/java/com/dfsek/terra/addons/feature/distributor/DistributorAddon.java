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
import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class DistributorAddon implements MonadAddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<Distributor>>> DISTRIBUTOR_TOKEN = new TypeKey<>() {
    };
    
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                ((functionalEventHandler, base) -> Init.ofPure(
                        functionalEventHandler.register(base, ConfigPackPreLoadEvent.class)
                                              .then(event -> {
                                                  CheckedRegistry<Supplier<ObjectTemplate<Distributor>>> distributorRegistry = event
                                                          .getPack()
                                                          .getOrCreateRegistry(DISTRIBUTOR_TOKEN);
                            
                                                  distributorRegistry.register(base.key("SAMPLER"), SamplerDistributorTemplate::new);
                                                  distributorRegistry.register(base.key("POINTS"), PointSetDistributorTemplate::new);
                                                  distributorRegistry.register(base.key("PADDED_GRID"), PaddedGridDistributorTemplate::new);
                                                  distributorRegistry.register(base.key("AND"), AndDistributorTemplate::new);
                                                  distributorRegistry.register(base.key("OR"), OrDistributorTemplate::new);
                                                  distributorRegistry.register(base.key("XOR"), XorDistributorTemplate::new);
                                                  distributorRegistry.register(base.key("YES"), YesDistributorTemplate::new);
                                                  distributorRegistry.register(base.key("NO"), NoDistributorTemplate::new);
                            
                                                  event.getPack()
                                                       .applyLoader(Point.class, PointTemplate::new);
                                              })
                                              .failThrough()
                                                                        )));
    }
}
