package com.dfsek.terra.addons.manifest.api;

import com.dfsek.terra.addons.manifest.api.monad.Init;


public interface MonadAddonInitializer {
    Init<?> initialize();
}
