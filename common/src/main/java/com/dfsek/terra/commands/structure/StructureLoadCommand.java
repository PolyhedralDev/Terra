package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Switch;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.command.arg.IntegerArgumentParser;
import com.dfsek.terra.api.platform.CommandSender;

@Command(
        arguments = {
                @Argument(
                        value = "rotation",
                        required = false,
                        tabCompleter = RotationCompleter.class,
                        argumentParser = IntegerArgumentParser.class
                )
        },
        switches = {
                @Switch(value = "chunk",
                        aliases = "c"
                )
        }
)
public class StructureLoadCommand implements CommandTemplate {
    @ArgumentTarget("rotation")
    private Integer rotation;

    @Override
    public void execute(CommandSender sender) {
        System.out.println(rotation);
    }
}
