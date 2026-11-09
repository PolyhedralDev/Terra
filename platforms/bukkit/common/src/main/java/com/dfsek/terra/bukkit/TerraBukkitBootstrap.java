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
package com.dfsek.terra.bukkit;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;

import com.dfsek.terra.bukkit.world.BiomeHandler;


/**
 * Paper only offers tag lifecycle events to a bootstrapper.
 */
public class TerraBukkitBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.postFlatten(RegistryKey.BIOME), event -> {
            if(event.cause() == ReloadableRegistrarEvent.Cause.RELOAD) BiomeHandler.addToReloadedTags(event.registrar());
        });
    }
}
