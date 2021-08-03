package com.dfsek.terra.sponge;

import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

@Plugin("terra")
public class TerraSpongePlugin {
    private final PluginContainer plugin;
    private final TerraPluginImpl terraPlugin;

    @Inject
    public TerraSpongePlugin(PluginContainer plugin, Game game) {
        this.plugin = plugin;
        this.terraPlugin = new TerraPluginImpl(this);
        game.eventManager().registerListeners(plugin, new SpongeListener(this));
    }

    public PluginContainer getPluginContainer() {
        return plugin;
    }

    public TerraPluginImpl getTerraPlugin() {
        return terraPlugin;
    }
}
