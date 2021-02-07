package com.dfsek.terra.bukkit.command.command;

import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class GetBlockCommand extends WorldCommand {
    public GetBlockCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull Command command, @NotNull String s, @NotNull String[] strings, World world) {
        player.sendMessage("Block: " + getMain().getWorld(BukkitAdapter.adapt(world)).getUngeneratedBlock(BukkitAdapter.adapt(player.getLocation())).getAsString());
        return true;
    }

    @Override
    public String getName() {
        return "block";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
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
