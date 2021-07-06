package com.dfsek.terra.api.command;

import com.dfsek.terra.api.entity.CommandSender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ExecutionState {
    private final Set<String> switches = new HashSet<>();
    private final Map<String, String> args = new HashMap<>();
    private final CommandSender sender;

    protected ExecutionState(CommandSender sender) {
        this.sender = sender;
    }

    protected void addSwitch(String flag) {
        switches.add(flag);
    }

    protected void addArgument(String arg, String value) {
        args.put(arg, value);
    }

    public String getArgument(String argument) {
        return args.get(argument);
    }

    public boolean hasSwitch(String flag) {
        return switches.contains(flag);
    }

    public CommandSender getSender() {
        return sender;
    }
}
