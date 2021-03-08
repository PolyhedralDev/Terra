package com.dfsek.terra.api.command;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.command.annotation.Switch;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.command.exception.InvalidArgumentsException;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.api.command.exception.SwitchFormatException;
import com.dfsek.terra.api.injection.Injector;
import com.dfsek.terra.api.injection.exception.InjectionException;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.world.TerraWorld;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TerraCommandManager implements CommandManager {
    private final Map<String, CommandHolder> commands = new HashMap<>();
    private final Injector<TerraPlugin> pluginInjector;
    private final TerraPlugin main;

    public TerraCommandManager(TerraPlugin main) {
        this.main = main;
        this.pluginInjector = new Injector<>(main);
        pluginInjector.addExplicitTarget(TerraPlugin.class);
    }

    @Override
    public void execute(String commandName, CommandSender sender, List<String> argsIn) throws CommandException {
        execute(commands.get(commandName), sender, new ArrayList<>(argsIn));
    }

    private void execute(CommandHolder commandHolder, CommandSender sender, List<String> args) throws CommandException {
        Class<? extends CommandTemplate> commandClass = commandHolder.clazz;

        if(commandClass.isAnnotationPresent(DebugCommand.class) && !main.isDebug()) {
            sender.sendMessage("Command must be executed with debug mode enabled.");
            return;
        }

        if(commandClass.isAnnotationPresent(PlayerCommand.class) && !(sender instanceof Player)) {
            sender.sendMessage("Command must be executed by player.");
            return;
        }

        if(commandClass.isAnnotationPresent(WorldCommand.class) && (!(sender instanceof Player) || !TerraWorld.isTerraWorld(((Player) sender).getWorld()))) {
            sender.sendMessage("Command must be executed in a Terra world.");
            return;
        }

        List<String> ogArgs = new ArrayList<>(args);

        ExecutionState state = new ExecutionState(sender);

        if(!commandClass.isAnnotationPresent(Command.class)) {
            invoke(commandClass, state);
            return;
        }

        Command command = commandClass.getAnnotation(Command.class);

        if(command.arguments().length == 0 && command.subcommands().length == 0) {
            if(args.isEmpty()) {
                invoke(commandClass, state);
                return;
            } else throw new InvalidArgumentsException("Expected 0 arguments, found " + args.size());
        }

        if(!args.isEmpty() && commandHolder.subcommands.containsKey(args.get(0))) {
            String c = args.get(0);
            args.remove(0);
            execute(commandHolder.subcommands.get(c), sender, args);
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

            if(arg.startsWith("-")) { // switches have started.
                if(req) throw new InvalidArgumentsException("Switches must come after arguments.");
                break;
            }

            state.addArgument(argument.value(), args.remove(0));
        }

        while(!args.isEmpty()) {
            String aSwitch = args.remove(0);
            if(!aSwitch.startsWith("-")) throw new SwitchFormatException("Invalid switch \"" + aSwitch + "\"");

            String val = aSwitch.substring(1); // remove dash

            if(!commandHolder.switches.containsKey(val)) throw new SwitchFormatException("No such switch \"" + aSwitch + "\"");

            state.addSwitch(commandHolder.switches.get(val));
        }

        invoke(commandClass, state);
    }

    private void invoke(Class<? extends CommandTemplate> clazz, ExecutionState state) throws MalformedCommandException {
        try {
            System.out.println("invocation");
            CommandTemplate template = clazz.getConstructor().newInstance();

            pluginInjector.inject(template);

            template.execute(state);
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | InjectionException e) {
            throw new MalformedCommandException("Unable to reflectively instantiate command: ", e);
        }
    }

    @Override
    public void register(String name, Class<? extends CommandTemplate> clazz) {
        commands.put(name, new CommandHolder(clazz));
    }

    @Override
    public List<String> tabComplete(String command, CommandSender sender, List<String> args) throws CommandException {
        if(args.isEmpty()) return new ArrayList<>(commands.keySet()).stream().sorted(String::compareTo).collect(Collectors.toList());
        return tabComplete(commands.get(command), sender, new ArrayList<>(args)).stream().filter(s -> s.toLowerCase().startsWith(args.get(args.size() - 1).toLowerCase())).sorted(String::compareTo).collect(Collectors.toList());
    }

    private List<String> tabComplete(CommandHolder holder, CommandSender sender, List<String> args) throws CommandException {
        if(args.isEmpty()) return Collections.emptyList();
        List<String> completions = new ArrayList<>();

        if(args.size() == 1) {
            completions.addAll(holder.subcommands.keySet());
        }

        if(holder.subcommands.containsKey(args.get(0))) {
            List<String> newArgs = new ArrayList<>(args);
            newArgs.remove(0);
            completions.addAll(tabComplete(holder.subcommands.get(args.get(0)), sender, newArgs));
        }
        try {
            if(args.size() <= holder.arguments.size()) {
                completions.addAll(holder.arguments.get(args.size() - 1).tabCompleter().getConstructor().newInstance().complete(sender));
            }
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MalformedCommandException("Unable to reflectively instantiate tab-completer: ", e);
        }
        return completions;
    }

    /**
     * Pre-processes command metadata.
     */
    private static final class CommandHolder {
        private final Class<? extends CommandTemplate> clazz;
        private final Map<String, CommandHolder> subcommands = new HashMap<>();
        private final Map<String, String> switches = new HashMap<>();
        private final List<Argument> arguments;

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
                for(Switch aSwitch : command.switches()) {
                    switches.put(aSwitch.value(), aSwitch.value());
                    for(String alias : aSwitch.aliases()) {
                        switches.put(alias, aSwitch.value());
                    }
                }
                arguments = Arrays.asList(command.arguments());
            } else arguments = Collections.emptyList();
        }
    }
}
