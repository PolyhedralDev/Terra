package com.dfsek.terra.command.type;

import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class WorldCommand extends PlayerCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.getWorld().getGenerator() instanceof TerraChunkGenerator) {
            return onCommand(sender, command, label, args, sender.getWorld());
        } else {
            sender.sendMessage("World is not a Terra world!");
        }
        return true;
    }

    public abstract boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world);
}
