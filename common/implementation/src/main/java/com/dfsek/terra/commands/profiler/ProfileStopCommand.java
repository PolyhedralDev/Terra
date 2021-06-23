package com.dfsek.terra.commands.profiler;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.entity.CommandSender;

@Command
@DebugCommand
public class ProfileStopCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        main.getProfiler().stop();
        sender.sendMessage("Profiling disabled.");
    }
}
