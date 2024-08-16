package com.dfsek.terra.lifecycle;

import net.minecraft.server.command.ServerCommandSource;
import org.incendo.cloud.fabric.FabricServerCommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;


public final class LifecycleEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(LifecycleEntryPoint.class);

    public static void initialize(String modName, LifecyclePlatform platform) {
        logger.info("Initializing Terra {} mod...", modName);

        FabricServerCommandManager<CommandSender> manager = new FabricServerCommandManager<>(
            CommandExecutionCoordinator.simpleCoordinator(),
            serverCommandSource -> (CommandSender) serverCommandSource,
            commandSender -> (ServerCommandSource) commandSender
        );


        manager.brigadierManager().setNativeNumberSuggestions(false);

        platform.getEventManager().callEvent(new CommandRegistrationEvent(manager));
    }
}
