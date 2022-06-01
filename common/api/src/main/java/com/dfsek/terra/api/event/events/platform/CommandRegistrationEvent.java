package com.dfsek.terra.api.event.events.platform;

import cloud.commandframework.CommandManager;

import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.event.events.Event;


public class CommandRegistrationEvent implements Event {
    private final CommandManager<CommandSender> commandManager;
    
    public CommandRegistrationEvent(CommandManager<CommandSender> commandManager) {
        this.commandManager = commandManager;
    }
    
    public CommandManager<CommandSender> getCommandManager() {
        return commandManager;
    }
}
