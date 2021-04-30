package com.dfsek.terra.minestom.commands;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.TerraCommandManager;
import com.dfsek.terra.api.command.exception.CommandException;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.util.Arrays;
import java.util.List;

public class TerraCommand extends Command {
    public TerraCommand(TerraPlugin main) {
        super("terra", "te");

        CommandManager manager = new TerraCommandManager(main);

        ArgumentStringArray argument = ArgumentType.StringArray("args");

        addSyntax(((sender, context) -> {
            List<String> args = Arrays.asList(context.get(argument));
            try {
                manager.execute(args.remove(0), null, args);
            } catch(CommandException e) {
                e.printStackTrace();
            }
        }), argument);
    }
}
