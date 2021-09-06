package com.dfsek.terra.api.event.events.platform;

import cloud.commandframework.CommandManager;

import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.event.events.Event;


/**
 * Called when commands should be registered. Listen to this event to register your own commands.
 */
public class CommandRegistrationEvent implements Event {
    private final CommandManager<CommandSender> manager;
    
    public CommandRegistrationEvent(CommandManager<CommandSender> manager) {
        this.manager = manager;
    }
    
    public CommandManager<CommandSender> getManager() {
        return manager;
    }
}
