package com.dfsek.terra.bukkit.command.command;

import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.command.Command;
import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SaveDataCommand extends WorldCommand {
    public SaveDataCommand(Command parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "save-data";
    }

    @Override
    public List<Command> getSubCommands() {
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

    @Override
    public boolean execute(@NotNull Player sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        BukkitChunkGeneratorWrapper.saveAll();
        LangUtil.send("debug.data-save", new BukkitCommandSender(sender), w.getName());
        return true;
    }
}
