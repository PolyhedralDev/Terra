package com.dfsek.terra.command;

import com.dfsek.terra.command.type.Command;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SaveDataCommand extends Command {
    @Override
    public String getName() {
        return "save-data";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TerraChunkGenerator.saveAll();
        sender.sendMessage("Saved population data.");
        return true;
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public List<Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
