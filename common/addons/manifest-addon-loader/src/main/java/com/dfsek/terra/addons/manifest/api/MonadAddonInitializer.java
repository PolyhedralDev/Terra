package com.dfsek.terra.addons.manifest.api;

import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.util.function.monad.Monad;


public interface MonadAddonInitializer {
    Monad<?, Init<?>> initialize();
}
