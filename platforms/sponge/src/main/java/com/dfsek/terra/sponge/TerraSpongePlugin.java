/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

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
    public TerraSpongePlugin(Game game) {
        this.plugin = null;
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
