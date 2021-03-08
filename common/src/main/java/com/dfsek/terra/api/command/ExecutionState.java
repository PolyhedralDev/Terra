package com.dfsek.terra.api.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ExecutionState {
    private final Set<String> switches = new HashSet<>();
    private final Map<String, String> args = new HashMap<>();

    protected ExecutionState() {

    }

    protected void addSwitch(String flag) {
        switches.add(flag);
    }

    protected void addArgument(String arg, String value) {
        args.put(arg, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getArgument(String argument, Class<T> clazz) {

        String value = args.get(argument);
        if(value == null) throw new IllegalArgumentException("Argument \"" + argument + "\" does not exist!");

        if(clazz == int.class || clazz == Integer.class) {
            return (T) new Integer(Integer.parseInt(value));
        }
        if(clazz == double.class || clazz == Double.class) {
            return (T) new Double(Double.parseDouble(value));
        }

        // TODO: type loaders

        return (T) value;
    }

    public boolean hasSwitch(String flag) {
        return switches.contains(flag);
    }
}
