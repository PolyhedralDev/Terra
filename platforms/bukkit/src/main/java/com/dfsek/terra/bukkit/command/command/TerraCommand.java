package com.dfsek.terra.bukkit.command.command;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.command.Command;
import com.dfsek.terra.bukkit.command.command.biome.BiomeCommand;
import com.dfsek.terra.bukkit.command.command.geometry.GeometryCommand;
import com.dfsek.terra.bukkit.command.command.profile.ProfileCommand;
import com.dfsek.terra.bukkit.command.command.structure.StructureCommand;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TerraCommand extends Command {
    private final List<Command> commands = Arrays.asList(new ReloadCommand(this),
            new BiomeCommand(this),
            new ProfileCommand(this),
            new SaveDataCommand(this),
            new StructureCommand(this),
            new GeometryCommand(this),
            new FixChunkCommand(this),
            new VersionCommand(this),
            new PacksCommand(this));

    public TerraCommand(TerraPlugin main) {
        super(main);
    }

    @Override
    public String getName() {
        return "com/dfsek/terra";
    }

    @Override
    public List<Command> getSubCommands() {
        return commands;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        LangUtil.send("command.main-menu", new BukkitCommandSender(sender));
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
