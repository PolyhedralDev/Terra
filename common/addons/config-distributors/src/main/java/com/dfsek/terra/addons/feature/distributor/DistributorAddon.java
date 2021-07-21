package com.dfsek.terra.addons.feature.distributor;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.distributor.config.NoiseDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.config.PointSetDistributorTemplate;
import com.dfsek.terra.addons.feature.distributor.util.Point;
import com.dfsek.terra.addons.feature.distributor.util.PointTemplate;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.util.reflection.TypeKey;

import java.util.function.Supplier;

@Addon("config-distributors")
@Version("1.0.0")
@Author("Terra")
public class DistributorAddon extends TerraAddon implements EventListener {
    public static final TypeKey<Supplier<ObjectTemplate<Distributor>>> DISTRIBUTOR_TOKEN = new TypeKey<>() {};
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }


    public void packPreLoad(ConfigPackPreLoadEvent event) {
        CheckedRegistry<Supplier<ObjectTemplate<Distributor>>> distributorRegistry = event.getPack().getOrCreateRegistry(DISTRIBUTOR_TOKEN);
        distributorRegistry.register("NOISE", NoiseDistributorTemplate::new);
        distributorRegistry.register("POINTS", PointSetDistributorTemplate::new);

        event.getPack()
                .applyLoader(Point.class, PointTemplate::new);
    }
}
