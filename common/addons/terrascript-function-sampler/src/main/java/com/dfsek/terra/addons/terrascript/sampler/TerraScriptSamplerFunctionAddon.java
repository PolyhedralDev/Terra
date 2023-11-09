package com.dfsek.terra.addons.terrascript.sampler;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.noise.NoiseConfigPackTemplate;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


public class TerraScriptSamplerFunctionAddon implements AddonInitializer {
    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;


    @Override
    public void initialize() {
        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, ConfigPackPreLoadEvent.class)
            .priority(51)
            .then(event -> event
                .getPack()
                .getOrCreateRegistry(FunctionBuilder.class)
                .register(addon.key("sampler"), new SamplerFunctionBuilder(event.getPack().getContext().get(
                    NoiseConfigPackTemplate.class).getSamplers())))
            .failThrough();
    }
}
