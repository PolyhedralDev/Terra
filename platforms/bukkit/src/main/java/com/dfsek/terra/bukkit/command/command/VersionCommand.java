package com.dfsek.terra.bukkit.command.command;

import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.bukkit.command.Command;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class VersionCommand extends Command {
    public VersionCommand(Command parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public List<Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String terraVersion = ((TerraBukkitPlugin) getMain()).getDescription().getVersion();
        LangUtil.send("command.version", new BukkitCommandSender(sender), terraVersion, getMain().platformName());
        return true;
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
