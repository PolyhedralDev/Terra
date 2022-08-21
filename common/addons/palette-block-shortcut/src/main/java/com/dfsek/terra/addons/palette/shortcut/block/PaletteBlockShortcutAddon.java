package com.dfsek.terra.addons.palette.shortcut.block;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;

import org.jetbrains.annotations.NotNull;


public class PaletteBlockShortcutAddon implements MonadAddonInitializer {
    
    @Override
    public @NotNull Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((handler, base, platform) -> Init.ofPure(
                        handler.register(base, ConfigPackPreLoadEvent.class)
                               .then(event -> event.getPack()
                                                   .registerShortcut(Palette.class, "BLOCK",
                                                                     (configLoader, input, tracker) -> new SingletonPalette(
                                                                             configLoader.loadType(BlockState.class, input, tracker))))
                               .failThrough()))
                      );
        
    }
}
