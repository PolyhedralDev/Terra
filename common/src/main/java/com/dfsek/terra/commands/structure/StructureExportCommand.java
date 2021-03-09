package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.platform.CommandSender;

@Command
public class StructureExportCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {
        System.out.println("export command");
    }
}
