package com.dfsek.terra.api.command;

import com.dfsek.terra.api.platform.CommandSender;

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

    @SuppressWarnings("unchecked")
    public <T> T getArgument(String argument, Class<T> clazz) {

        Object value = args.get(argument);
        if(value == null) throw new IllegalArgumentException("Argument \"" + argument + "\" does not exist!");

        if(clazz == int.class || clazz == Integer.class) {
            value = Integer.parseInt(value.toString());
        } else if(clazz == double.class || clazz == Double.class) {
            value = Double.parseDouble(value.toString());
        }

        // TODO: type loaders

        return clazz.cast(value);
    }

    public boolean hasSwitch(String flag) {
        return switches.contains(flag);
    }

    public CommandSender getSender() {
        return sender;
    }
}
