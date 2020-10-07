package com.dfsek.terra.command;

import com.dfsek.terra.command.type.Command;
import com.dfsek.terra.command.type.WorldCommand;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SaveDataCommand extends WorldCommand {
    @Override
    public String getName() {
        return "save-data";
    }

    @Override
    public boolean execute(@NotNull Player sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        TerraChunkGenerator.saveAll();
        LangUtil.send("debug.data-save", sender, w.getName());
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
