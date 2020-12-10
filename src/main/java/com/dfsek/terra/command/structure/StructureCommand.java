package com.dfsek.terra.command.structure;

import com.dfsek.terra.api.gaea.command.PlayerCommand;
import com.dfsek.terra.command.structure.load.LoadCommand;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StructureCommand extends PlayerCommand {
    public StructureCommand(com.dfsek.terra.api.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        LangUtil.send("command.structure.main-menu", sender);
        return true;
    }

    @Override
    public String getName() {
        return "structure";
    }

    @Override
    public List<com.dfsek.terra.api.gaea.command.Command> getSubCommands() {
        return Arrays.asList(new ExportCommand(this), new LoadCommand(this), new LocateCommand(this, false), new LocateCommand(this, true), new SpawnCommand(this));
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
