package com.dfsek.terra.commands;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.commands.structure.SpawnCommand;
import com.dfsek.terra.commands.structure.StructureExportCommand;
import com.dfsek.terra.commands.structure.StructureLoadCommand;

@Command(
        subcommands = {
                @Subcommand(
                        clazz = StructureExportCommand.class,
                        value = "export",
                        aliases = "ex"
                ),
                @Subcommand(
                        clazz = StructureLoadCommand.class,
                        value = "load",
                        aliases = "ld"
                ),
                @Subcommand(
                        clazz = SpawnCommand.class,
                        value = "spawn",
                        aliases = "s"
                )
        },
        usage = "/te structure"
)
public class StructureCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {

    }
}
