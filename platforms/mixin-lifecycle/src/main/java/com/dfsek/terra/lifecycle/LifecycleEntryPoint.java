package com.dfsek.terra.lifecycle;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.fabric.FabricServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;


public class LifecycleEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(LifecycleEntryPoint.class);
    
    protected static void initialize(String modName, LifecyclePlatform platform) {
        logger.info("Initializing Terra {} mod...", modName);
        
        try {
            FabricServerCommandManager<CommandSender> manager = new FabricServerCommandManager<>(
                    CommandExecutionCoordinator.simpleCoordinator(),
                    serverCommandSource -> (CommandSender) serverCommandSource,
                    commandSender -> (ServerCommandSource) commandSender
            );
    
    
            manager.brigadierManager().setNativeNumberSuggestions(false);
    
            platform.getEventManager().callEvent(new CommandRegistrationEvent(manager));
        } catch (Exception e) {
            logger.warn("Fabric API not found, Terra commands will not work.");
        }
    }
}