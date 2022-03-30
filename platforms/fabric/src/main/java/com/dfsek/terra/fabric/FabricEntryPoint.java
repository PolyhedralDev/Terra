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

package com.dfsek.terra.fabric;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.fabric.FabricServerCommandManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.fabric.data.Codecs;


public class FabricEntryPoint implements ModInitializer {
    private static final Logger logger = LoggerFactory.getLogger(FabricEntryPoint.class);
    
    private static final PlatformImpl TERRA_PLUGIN = new PlatformImpl();
    
    
    public static PlatformImpl getPlatform() {
        return TERRA_PLUGIN;
    }
    
    public static void register() { // register the things
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("terra:terra"), Codecs.FABRIC_CHUNK_GENERATOR_WRAPPER);
        Registry.register(Registry.BIOME_SOURCE, new Identifier("terra:terra"), Codecs.TERRA_BIOME_SOURCE);
    }
    
    @Override
    public void onInitialize() {
        logger.info("Initializing Terra Fabric mod...");

        FabricServerCommandManager<CommandSender> manager = new FabricServerCommandManager<>(
                CommandExecutionCoordinator.simpleCoordinator(),
                serverCommandSource -> (CommandSender) serverCommandSource,
                commandSender -> (ServerCommandSource) commandSender
        );


        manager.brigadierManager().setNativeNumberSuggestions(false);

        TERRA_PLUGIN.getEventManager().callEvent(new CommandRegistrationEvent(manager));
    }
}
