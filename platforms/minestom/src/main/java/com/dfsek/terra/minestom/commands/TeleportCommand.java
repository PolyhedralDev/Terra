package com.dfsek.terra.minestom.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Entity;
import net.minestom.server.utils.Position;

public class TeleportCommand extends Command {
    public TeleportCommand() {
        super("teleport", "tp");

        Argument<Double> x = ArgumentType.Double("x");
        Argument<Double> y = ArgumentType.Double("y");
        Argument<Double> z = ArgumentType.Double("z");

        addSyntax((sender, context) -> ((Entity) sender).teleport(new Position(context.get(x), context.get(y), context.get(z))), x, y, z);
    }
}
