package com.dfsek.terra.bukkit.command.command;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.command.Command;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.pack.ConfigPackTemplate;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class PacksCommand extends Command {
    public PacksCommand(Command parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "packs";
    }

    @Override
    public List<Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        CheckedRegistry<ConfigPack> registry = getMain().getConfigRegistry();

        if(registry.entries().size() == 0) {
            LangUtil.send("command.packs.none", new BukkitCommandSender(commandSender));
            return true;
        }

        LangUtil.send("command.packs.main", new BukkitCommandSender(commandSender));
        registry.entries().forEach(entry -> {
            ConfigPackTemplate template = entry.getTemplate();
            LangUtil.send("command.packs.pack", new BukkitCommandSender(commandSender), template.getID(), template.getAuthor(), template.getVersion());
        });

        return true;
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
