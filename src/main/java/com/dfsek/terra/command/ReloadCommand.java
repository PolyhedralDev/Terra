package com.dfsek.terra.command;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.config.base.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.registry.ConfigRegistry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.Command;
import org.polydev.gaea.command.DebugCommand;

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
        PluginConfig.load(getMain());
        LangUtil.load(PluginConfig.getLanguage(), getMain()); // Load language.
        if(!ConfigRegistry.loadAll(getMain())) LangUtil.send("command.reload-error", sender);
        TerraWorld.invalidate();
        LangUtil.send("command.reload", sender);
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
