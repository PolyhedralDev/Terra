package com.dfsek.terra.bukkit.command.command;

import com.dfsek.terra.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class AddonsCommand extends Command {
    public AddonsCommand(Command parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "addons";
    }

    @Override
    public List<Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("Installed Addons:");
        getMain().getAddons().forEach(addon -> sender.sendMessage(" - " + addon.getName() + " v" + addon.getVersion() + " by " + addon.getAuthor()));
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
