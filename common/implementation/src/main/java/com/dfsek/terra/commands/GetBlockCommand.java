package com.dfsek.terra.commands;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;

@WorldCommand
@DebugCommand
@PlayerCommand
@Command(
        usage = "/terra getblock"
)
public class GetBlockCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        sender.sendMessage("Block: " + main.getWorld(player.world()).getUngeneratedBlock(player.position()).getAsString());
    }
}
