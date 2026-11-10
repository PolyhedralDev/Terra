package com.dfsek.terra.bukkit.hooks;

import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.generators.GeneratorPlugin;
import org.mvplugins.multiverse.external.jetbrains.annotations.NotNull;
import org.mvplugins.multiverse.external.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Stream;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.key.Keyed;


public final class MultiverseGeneratorPluginHook implements GeneratorPlugin {

    private final Platform platform;

    public MultiverseGeneratorPluginHook(Platform platform) {
        this.platform = platform;
    }

    public static void register(Platform platform) {
        MultiverseCoreApi.get().getGeneratorProvider()
            .registerGeneratorPlugin(new MultiverseGeneratorPluginHook(platform));
    }

    @Override
    public @NotNull Collection<String> suggestIds(@Nullable String s) {
        return ids().toList();
    }

    @Override
    public @Nullable Collection<String> getExampleUsages() {
        return ids()
            .map("/mv create example_world NORMAL -g Terra:%s"::formatted)
            .limit(5) // reasonable amount
            .toList();
    }

    private Stream<String> ids() {
        return Stream.concat(platform.getConfigRegistry().entries().stream().map(Keyed::getID),
                platform.getMetaConfigRegistry().entries().stream().map(Keyed::getID))
            .distinct();
    }

    @Override
    public @Nullable String getInfoLink() {
        return "https://terra.polydev.org/";
    }

    @Override
    public @NotNull String getPluginName() {
        return "Terra";
    }
}
