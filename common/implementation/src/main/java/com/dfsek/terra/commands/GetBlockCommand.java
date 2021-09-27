package com.dfsek.terra.commands;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.inject.annotations.Inject;


@WorldCommand
@DebugCommand
@PlayerCommand
@Command(
        usage = "/terra getblock"
)
public class GetBlockCommand implements CommandTemplate {
    @Inject
    private Platform platform;
    
    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        sender.sendMessage("Block: " + player.world().getGenerator().getBlock(player.world(), player.position()).getAsString());
    }
}
