package com.dfsek.terra.commands;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.ExecutionState;
import com.dfsek.terra.api.command.annotation.Command;

@Command()
public class ReloadCommand implements CommandTemplate {
    @Override
    public void execute(ExecutionState state) {

    }
}
