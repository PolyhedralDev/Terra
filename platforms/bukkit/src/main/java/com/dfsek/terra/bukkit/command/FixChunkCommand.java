package com.dfsek.terra.bukkit.command;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;

@Command
@WorldCommand
@PlayerCommand
public class FixChunkCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        BukkitChunkGeneratorWrapper.fixChunk(player.getWorld().getChunkAt(player.getLocation()));
    }
}
