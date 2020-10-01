package com.dfsek.terra.command.image.gui;

import com.dfsek.terra.command.WorldCommand;
import com.dfsek.terra.config.WorldConfig;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class GUICommand extends WorldCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        sender.sendMessage("-------------Terra/image/gui-------------");
        sender.sendMessage("raw  - Open GUI with raw Biome data");
        sender.sendMessage("step - Re-render data to show borders more clearly");
        return true;
    }

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return Arrays.asList(new StepGUICommand(), new RawGUICommand());
    }

    @Override
    public int arguments() {
        return 1;
    }
}
