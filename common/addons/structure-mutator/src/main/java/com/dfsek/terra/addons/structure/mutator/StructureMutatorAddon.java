package com.dfsek.terra.addons.structure.mutator;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.function.monad.Monad;


public class StructureMutatorAddon implements MonadAddonInitializer {
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                ((handler, base) -> Init.ofPure(
                        handler.register(base, ConfigPackPreLoadEvent.class)
                               .then(event -> {
                                   event.getPack().registerConfigType(new MutatedStructureConfigType(base), base.key("MUTATED_STRUCTURE"), 499);
                               })
                               .failThrough()))
                      );
    }
}
