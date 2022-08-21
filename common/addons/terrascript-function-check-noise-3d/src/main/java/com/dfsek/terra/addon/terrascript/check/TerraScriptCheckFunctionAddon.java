package com.dfsek.terra.addon.terrascript.check;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.util.function.monad.Monad;

import org.jetbrains.annotations.NotNull;

import static com.dfsek.terra.addons.manifest.api.monad.Get.platform;
import static com.dfsek.terra.addons.manifest.api.monad.Register.register;


public class TerraScriptCheckFunctionAddon implements MonadAddonInitializer {
    @Override
    public @NotNull Monad<?, Init<?>> initialize() {
        return platform().bind(platform -> register(FunctionBuilder.class, "check", new CheckFunctionBuilder(platform)));
    }
}
