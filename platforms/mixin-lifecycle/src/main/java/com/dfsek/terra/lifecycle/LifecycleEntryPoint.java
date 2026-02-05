package com.dfsek.terra.lifecycle;

import net.minecraft.server.command.ServerCommandSource;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;


public final class LifecycleEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(LifecycleEntryPoint.class);

    public static void initialize(String modName, LifecyclePlatform platform) {
        logger.info("Initializing Terra {} mod...", modName);

        CommandManager<CommandSender> manager = createCommandManager();
        if(manager == null) {
            logger.warn("Cloud Fabric command manager not available; skipping Terra command registration.");
            return;
        }

        disableNativeNumberSuggestions(manager);
        platform.getEventManager().callEvent(new CommandRegistrationEvent(manager));
    }

    @SuppressWarnings("unchecked")
    private static CommandManager<CommandSender> createCommandManager() {
        try {
            Class<?> managerClass = Class.forName("org.incendo.cloud.fabric.FabricServerCommandManager");
            Constructor<?> constructor = managerClass.getConstructor(ExecutionCoordinator.class, SenderMapper.class);
            Object instance = constructor.newInstance(
                ExecutionCoordinator.simpleCoordinator(),
                SenderMapper.create(
                    serverCommandSource -> (CommandSender) serverCommandSource,
                    commandSender -> (ServerCommandSource) commandSender
                )
            );
            return (CommandManager<CommandSender>) instance;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (ReflectiveOperationException e) {
            logger.error("Failed to initialize Fabric command manager via reflection.", e);
            return null;
        }
    }

    private static void disableNativeNumberSuggestions(CommandManager<CommandSender> manager) {
        try {
            Method brigadierManager = manager.getClass().getMethod("brigadierManager");
            Object brigadier = brigadierManager.invoke(manager);
            Method setNativeNumberSuggestions = brigadier.getClass().getMethod("setNativeNumberSuggestions", boolean.class);
            setNativeNumberSuggestions.invoke(brigadier, false);
        } catch (ReflectiveOperationException e) {
            logger.warn("Unable to disable native number suggestions for command manager.", e);
        }
    }
}
