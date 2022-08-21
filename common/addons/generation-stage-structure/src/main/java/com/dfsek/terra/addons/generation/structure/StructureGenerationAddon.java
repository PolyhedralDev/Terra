package com.dfsek.terra.addons.generation.structure;

import com.dfsek.terra.addons.generation.structure.config.BiomeStructuresTemplate;
import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.util.generic.Construct;
import com.dfsek.terra.api.world.biome.Biome;

import org.jetbrains.annotations.NotNull;


public class StructureGenerationAddon implements MonadAddonInitializer {
    @Override
    public @NotNull Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((handler, base, platform) -> Init.ofPure(Construct.construct(() -> {
                    handler.register(base, ConfigPackPreLoadEvent.class)
                           .then(event -> event.getPack()
                                               .createRegistry(GenerationStageProvider.class)
                                               .register(base.key("STRUCTURE"), pack -> new StructureGenerationStage(platform)))
                           .failThrough();
                    return handler.register(base, ConfigurationLoadEvent.class)
                                  .then(event -> {
                                      if(event.is(Biome.class)) {
                                          event.getLoadedObject(Biome.class).getContext().put(
                                                  event.load(new BiomeStructuresTemplate()).get());
                                      }
                                  })
                                  .failThrough();
                }))));
    }
}
