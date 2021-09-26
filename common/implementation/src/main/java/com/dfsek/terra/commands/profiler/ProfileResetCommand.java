package com.dfsek.terra.commands.profiler;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.inject.annotations.Inject;


@Command
@DebugCommand
public class ProfileResetCommand implements CommandTemplate {
    @Inject
    private Platform main;
    
    @Override
    public void execute(CommandSender sender) {
        main.getProfiler().reset();
        sender.sendMessage("Profiler reset.");
    }
}
