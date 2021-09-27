package com.dfsek.terra.sponge;

import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;


@Plugin("terra")
public class TerraSpongePlugin {
    private final PluginContainer plugin;
    private final PlatformImpl terraPlugin;
    
    @Inject
    public TerraSpongePlugin(PluginContainer plugin, Game game) {
        this.plugin = plugin;
        this.terraPlugin = new PlatformImpl(this);
        game.eventManager().registerListeners(plugin, new SpongeListener(this));
    }
    
    public PluginContainer getPluginContainer() {
        return plugin;
    }
    
    public PlatformImpl getTerraPlugin() {
        return terraPlugin;
    }
}
