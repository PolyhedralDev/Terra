package com.dfsek.terra.addons.feature.distributor;

import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.feature.distributor.config.AndDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.NoiseDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.OrDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.PointSetDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.YesDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.util.Point;
import com.dfsek.terra.addons.feature.distributor.util.PointTemplate;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.util.reflection.TypeKey;


@Addon("config-distributors")
@Version("1.0.0")
@Author("Terra")
public class DistributorAddon extends TerraAddon {
    public static final TypeKey<Supplier<ObjectTemplate<Distributor>>> DISTRIBUTOR_TOKEN = new TypeKey<>() {
    };
    @Inject
    private Platform main;
    
    @Override
    public void initialize() {
        main.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(this, ConfigPackPreLoadEvent.class)
            .then(event -> {
                CheckedRegistry<Supplier<ObjectTemplate<Distributor>>> distributorRegistry = event.getPack().getOrCreateRegistry(
                        DISTRIBUTOR_TOKEN);
                distributorRegistry.register("NOISE", NoiseDistributorTemplate::new);
                distributorRegistry.register("POINTS", PointSetDistributorTemplate::new);
                distributorRegistry.register("AND", AndDistributorTemplate::new);
                distributorRegistry.register("OR", OrDistributorTemplate::new);
                distributorRegistry.register("YES", YesDistributorTemplate::new);
                distributorRegistry.register("NO", NoiseDistributorTemplate::new);
            
                event.getPack()
                     .applyLoader(Point.class, PointTemplate::new);
            })
            .failThrough();
    }
}
