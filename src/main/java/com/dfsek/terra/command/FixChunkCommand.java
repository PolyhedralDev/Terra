package com.dfsek.terra.command;

import com.dfsek.terra.api.gaea.command.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class FixChunkCommand extends WorldCommand {
    public FixChunkCommand(com.dfsek.terra.api.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull Command command, @NotNull String s, @NotNull String[] strings, World world) {
        //TerraChunkGenerator.fixChunk(player.getLocation().getChunk());
        return true;
    }

    @Override
    public String getName() {
        return "fixchunk";
    }

    @Override
    public List<com.dfsek.terra.api.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
