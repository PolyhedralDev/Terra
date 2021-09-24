package com.dfsek.terra.commands.profiler;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.inject.annotations.Inject;


@Command
@DebugCommand
public class ProfileQueryCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;
    
    @Override
    public void execute(CommandSender sender) {
        StringBuilder data = new StringBuilder("Terra Profiler data dump: \n");
        main.getProfiler().getTimings().forEach((id, timings) -> data.append(id).append(": ").append(timings.toString()).append('\n'));
        main.logger().info(data.toString());
        sender.sendMessage("Profiler data dumped to console.");
    }
}
