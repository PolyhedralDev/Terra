package com.dfsek.terra.command.image.gui;

import com.dfsek.terra.command.WorldCommand;
import com.dfsek.terra.config.WorldConfig;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class StepGUICommand extends WorldCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        WorldConfig.fromWorld(world).imageLoader.debug(true, sender.getWorld());
        return true;
    }

    @Override
    public String getName() {
        return "step";
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 0;
    }
}
