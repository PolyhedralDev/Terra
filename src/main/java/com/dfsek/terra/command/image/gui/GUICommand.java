package com.dfsek.terra.command.image.gui;

import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.DebugCommand;
import org.polydev.gaea.command.WorldCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GUICommand extends WorldCommand implements DebugCommand {
    public GUICommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        LangUtil.send("command.image.gui.main-menu", sender);
        return true;
    }

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Arrays.asList(new StepGUICommand(this), new RawGUICommand(this));
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
