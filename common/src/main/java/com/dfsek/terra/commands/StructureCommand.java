package com.dfsek.terra.commands;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.ExecutionState;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
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
                )
        }
)
public class StructureCommand implements CommandTemplate {
    @Override
    public void execute(ExecutionState state) {

    }
}
