package com.dfsek.terra.bukkit.command.command.profile;

import com.dfsek.terra.api.profiler.WorldProfiler;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.BukkitWorld;
import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class StartCommand extends WorldCommand {
    public StartCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        WorldProfiler profile = getMain().getWorld(new BukkitWorld(world)).getProfiler();
        profile.setProfiling(true);
        LangUtil.send("command.profile.start", new BukkitCommandSender(sender));
        return true;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Collections.emptyList();
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
