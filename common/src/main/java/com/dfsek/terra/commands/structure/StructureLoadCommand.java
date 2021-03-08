package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.ExecutionState;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Switch;

@Command(
        arguments = {
                @Argument(
                        value = "rotation",
                        required = false,
                        type = int.class
                )
        },
        switches = {
                @Switch(value = "chunk",
                        aliases = "c"
                )
        }
)
public class StructureLoadCommand implements CommandTemplate {
    @Override
    public void execute(ExecutionState state) {

    }
}
