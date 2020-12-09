package com.dfsek.terra.command;

import com.dfsek.terra.Terra;
import com.dfsek.terra.config.lang.LangUtil;
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
        ((Terra) getMain()).getTerraConfig().load(getMain());
        LangUtil.load(((Terra) getMain()).getTerraConfig().getLanguage(), getMain()); // Load language.
        if(!((Terra) getMain()).getRegistry().loadAll((Terra) getMain())) LangUtil.send("command.reload-error", sender);
        ((Terra) getMain()).invalidate();
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
