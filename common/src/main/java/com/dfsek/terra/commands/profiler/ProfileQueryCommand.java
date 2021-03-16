package com.dfsek.terra.commands.profiler;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.world.TerraWorld;

@Command
@WorldCommand
@PlayerCommand
@DebugCommand
public class ProfileQueryCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        TerraWorld world = main.getWorld(player.getWorld());
        player.sendMessage(world.getProfiler().getResultsFormatted());
    }
}
