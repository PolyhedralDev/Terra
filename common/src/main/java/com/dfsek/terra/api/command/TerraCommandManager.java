package com.dfsek.terra.api.command;

import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.command.exception.InvalidArgumentsException;
import com.dfsek.terra.api.command.exception.MalformedCommandException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerraCommandManager implements CommandManager {
    private final Map<String, CommandHolder> commands = new HashMap<>();

    @Override
    public void execute(String commandName, List<String> argsIn) throws CommandException {
        execute(commands.get(commandName), new ArrayList<>(argsIn));
    }

    private void execute(CommandHolder commandHolder, List<String> args) throws CommandException {
        List<String> ogArgs = new ArrayList<>(args);

        ExecutionState state = new ExecutionState();

        Class<? extends CommandTemplate> commandClass = commandHolder.clazz;

        if(!commandClass.isAnnotationPresent(Command.class)) {
            invoke(commandClass, state);
        }

        Command command = commandClass.getAnnotation(Command.class);

        if(command.arguments().length == 0 && command.subcommands().length == 0) {
            if(args.isEmpty()) invoke(commandClass, state);
            else throw new InvalidArgumentsException("Expected 0 arguments, found " + args.size());
        }

        if(commandHolder.subcommands.containsKey(args.get(0))) {
            String c = args.get(0);
            args.remove(0);
            execute(commandHolder.subcommands.get(c), args);
            return;
        }


        boolean req = true;
        for(Argument argument : command.arguments()) {
            if(!req && argument.required()) {
                throw new MalformedCommandException("Required arguments must come first! Arguments: " + Arrays.toString(command.arguments()));
            }
            req = argument.required();

            if(args.isEmpty()) {
                if(req) throw new InvalidArgumentsException("Invalid arguments: " + ogArgs + ", usage: " + command.usage());
                break;
            }

            String arg = args.get(0);

            if(arg.startsWith("-")) { // flags have started.
                if(req) throw new InvalidArgumentsException("Flags must come after arguments.");
                break;
            }

            state.addArgument(argument.value(), args.remove(0));
        }


        invoke(commandClass, state);
    }

    private void invoke(Class<? extends CommandTemplate> clazz, ExecutionState state) throws MalformedCommandException {
        try {
            System.out.println("invocation");
            clazz.getConstructor().newInstance().execute(state);
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MalformedCommandException("Unable to reflectively instantiate command: ", e);
        }
    }

    @Override
    public void register(String name, Class<? extends CommandTemplate> clazz) {
        commands.put(name, new CommandHolder(clazz));
    }

    private static final class CommandHolder {
        private final Class<? extends CommandTemplate> clazz;
        private final Map<String, CommandHolder> subcommands = new HashMap<>();

        private CommandHolder(Class<? extends CommandTemplate> clazz) {
            this.clazz = clazz;
            if(clazz.isAnnotationPresent(Command.class)) {
                Command command = clazz.getAnnotation(Command.class);
                for(Subcommand subcommand : command.subcommands()) {
                    CommandHolder holder = new CommandHolder(subcommand.clazz());
                    subcommands.put(subcommand.value(), holder);
                    for(String alias : subcommand.aliases()) {
                        subcommands.put(alias, holder);
                    }
                }
            }
        }
    }
}
