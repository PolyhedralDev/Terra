package com.dfsek.terra.bukkit.command.command;


import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.command.Command;
import com.dfsek.terra.bukkit.command.DebugCommand;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command implements DebugCommand {
    public ReloadCommand(Command parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public List<Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        getMain().getTerraConfig().load(getMain());
        LangUtil.load(getMain().getTerraConfig().getLanguage(), getMain()); // Load language.
        if(!getMain().getRegistry().loadAll(getMain())) {
            LangUtil.send("command.reload-error", new BukkitCommandSender(sender));
            return true;
        }
        getMain().reload();
        LangUtil.send("command.reload", new BukkitCommandSender(sender));
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
