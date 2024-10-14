package com.dfsek.terra.api.event.events.platform;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.event.events.Event;

import org.incendo.cloud.CommandManager;


public class CommandRegistrationEvent implements Event {
    private final CommandManager<CommandSender> commandManager;

    public CommandRegistrationEvent(CommandManager<CommandSender> commandManager) {
        this.commandManager = commandManager;
    }

    public CommandManager<CommandSender> getCommandManager() {
        return commandManager;
    }
}
