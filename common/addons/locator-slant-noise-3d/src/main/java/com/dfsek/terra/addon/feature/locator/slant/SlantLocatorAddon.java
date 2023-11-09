package com.dfsek.terra.addon.feature.locator.slant;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class SlantLocatorAddon implements AddonInitializer {

    public static final TypeKey<Supplier<ObjectTemplate<Locator>>> LOCATOR_TOKEN = new TypeKey<>() {
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
            .priority(1)
            .then(event -> event.getPack().getOrCreateRegistry(LOCATOR_TOKEN).register(addon.key("SLANT"), SlantLocatorTemplate::new))
            .failThrough();
    }
}
