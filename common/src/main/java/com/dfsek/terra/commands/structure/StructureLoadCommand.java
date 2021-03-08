package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.ExecutionState;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Flag;

@Command(
        flags = {
                @Flag(value = "rotation",
                        defaultValue = "0",
                        shorthand = "r"
                ),
                @Flag(value = "load",
                        defaultValue = "FULL",
                        shorthand = "l"
                )
        }
)
public class StructureLoadCommand implements CommandTemplate {
    @Override
    public void execute(ExecutionState state) {

    }
}
