package com.dfsek.terra.sponge;

import com.google.inject.Inject;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

@Plugin("terra")
public class TerraSpongePlugin {
    private final PluginContainer plugin;
    private final TerraPluginImpl terraPlugin;
    @Inject
    public TerraSpongePlugin(PluginContainer plugin) {
        this.plugin = plugin;
        this.terraPlugin = new TerraPluginImpl(this);
    }

    public PluginContainer getPluginContainer() {
        return plugin;
    }

    public TerraPluginImpl getTerraPlugin() {
        return terraPlugin;
    }
}
