package com.dfsek.terra.addon.terrascript.check;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.function.monad.Monad;


public class TerraScriptCheckFunctionAddon implements MonadAddonInitializer {
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((handler, base, platform) -> Init.ofPure(
                        handler.register(base, ConfigPackPreLoadEvent.class)
                               .priority(1)
                               .then(event -> event
                                       .getPack()
                                       .getOrCreateRegistry(FunctionBuilder.class)
                                       .register(base.key("check"), new CheckFunctionBuilder(platform)))
                               .failThrough()))
                      );
    }
}
