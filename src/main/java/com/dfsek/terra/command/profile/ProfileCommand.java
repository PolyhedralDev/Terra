package com.dfsek.terra.command.profile;

import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.WorldCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProfileCommand extends WorldCommand {
    public ProfileCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        LangUtil.send("command.profile.main-menu", sender);
        return true;
    }

    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Arrays.asList(new QueryCommand(this), new ResetCommand(this), new StartCommand(this), new StopCommand(this));
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
