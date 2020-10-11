package com.dfsek.terra.command.structure;

import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.PlayerCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StructureCommand extends PlayerCommand {
    public StructureCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        LangUtil.send("command.structure.main-menu", sender);
        return true;
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Arrays.asList(new ExportCommand(this), new LoadCommand(this), new LocateCommand(this, false), new LocateCommand(this, true), new SpawnCommand(this));
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public String getName() {
        return "structure";
    }
}
