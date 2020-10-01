package com.dfsek.terra.command.structure;

import com.dfsek.terra.command.PlayerCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class StructureCommand extends PlayerCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("---------------Terra/structure---------------");
        sender.sendMessage("export - Export your current WorldEdit selection as a Terra structure.");
        sender.sendMessage("load   - Load a Terra structure");
        return true;
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return Arrays.asList(new ExportCommand(), new LoadCommand());
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public String getName() {
        return "structure";
    }
}
