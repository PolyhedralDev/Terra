package com.dfsek.terra.api.command;

import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerraCommandManager implements CommandManager {
    Map<String, CommandHolder> commands = new HashMap<>();

    @Override
    public void execute(String commandName, List<String> args) {

        ExecutionState state = new ExecutionState();

        CommandHolder commandHolder = commands.get(commandName);
        Class<? extends CommandTemplate> commandClass = commandHolder.clazz;

        if(!commandClass.isAnnotationPresent(Command.class)) {
            invoke(commandClass, state);
        }

        Command command = commandClass.getAnnotation(Command.class);

        if(command.arguments().length == 0 && command.subcommands().length == 0) {
            if(args.isEmpty()) invoke(commandClass, state);
            else throw new IllegalArgumentException("Expected 0 arguments, found " + args.size());
        }

        if(commandHolder.subcommands.containsKey(args.get(0))) {
            invoke(commandClass, state);
        }


        boolean req = true;
        for(Argument argument : command.arguments()) {
            if(!req && argument.required())
                throw new IllegalStateException("Required arguments must come first! Arguments: " + Arrays.toString(command.arguments()));
            req = argument.required();

            String arg = args.get(0);

            if(arg.startsWith("-")) { // flags have started.

            }

        }
    }

    private void invoke(Class<? extends CommandTemplate> clazz, ExecutionState state) {
        try {
            clazz.getConstructor().newInstance().execute(state);
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(String name, Class<? extends CommandTemplate> clazz) {
        commands.put(name, new CommandHolder(clazz));
    }

    private static final class CommandHolder {
        private final Class<? extends CommandTemplate> clazz;
        private final Map<String, Subcommand> subcommands = new HashMap<>();

        private CommandHolder(Class<? extends CommandTemplate> clazz) {
            this.clazz = clazz;
            if(clazz.isAnnotationPresent(Command.class)) {
                Command command = clazz.getAnnotation(Command.class);
                for(Subcommand subcommand : command.subcommands()) {
                    subcommands.put(subcommand.value(), subcommand);
                    for(String alias : subcommand.aliases()) {
                        subcommands.put(alias, subcommand);
                    }
                }
            }
        }
    }
}
