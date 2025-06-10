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

package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.bukkit.hooks.MultiverseGeneratorPluginHook;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Listener for events on all implementations.
 */
public class CommonListener implements Listener {
    private static final Logger logger = LoggerFactory.getLogger(CommonListener.class);
    private final Platform platform;

    public CommonListener(Platform platform) {
        this.platform = platform;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if(event.getPlugin().getName().equals("Multiverse-Core")) {
            try {
                MultiverseCoreApi.get().getGeneratorProvider()
                    .registerGeneratorPlugin(new MultiverseGeneratorPluginHook(platform));
            } catch (Exception e) {
                logger.error("Failed to register Terra generator plugin to multiverse.", e);
            }
        }
    }
}
