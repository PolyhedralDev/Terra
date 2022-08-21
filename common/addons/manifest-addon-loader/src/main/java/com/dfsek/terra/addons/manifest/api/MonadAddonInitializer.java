package com.dfsek.terra.addons.manifest.api;

import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.util.function.monad.Monad;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public interface MonadAddonInitializer {
    @NotNull
    @Contract(pure = true)
    Monad<?, Init<?>> initialize();
}
