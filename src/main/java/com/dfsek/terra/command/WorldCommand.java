package com.dfsek.terra.command;

import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class WorldCommand extends PlayerCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.getWorld().getGenerator() instanceof TerraChunkGenerator) {
            return onCommand(sender, command, label, args, sender.getWorld());
        }
        return true;
    }

    public abstract boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world);

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return null;
    }
}
