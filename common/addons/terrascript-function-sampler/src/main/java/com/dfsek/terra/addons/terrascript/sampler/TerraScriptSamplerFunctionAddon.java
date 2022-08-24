package com.dfsek.terra.addons.terrascript.sampler;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.addons.noise.NoiseConfigPackTemplate;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.function.monad.Monad;


public class TerraScriptSamplerFunctionAddon implements MonadAddonInitializer {
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                ((handler, base) -> Init.ofPure(
                        handler.register(base, ConfigPackPreLoadEvent.class)
                               .priority(51)
                               .then(event -> event
                                       .getPack()
                                       .getOrCreateRegistry(FunctionBuilder.class)
                                       .register(base.key("sampler"), new SamplerFunctionBuilder(event.getPack().getContext().get(
                                               NoiseConfigPackTemplate.class).getSamplers())))
                               .failThrough()))
                      );
    }
}
