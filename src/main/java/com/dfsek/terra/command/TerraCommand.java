package com.dfsek.terra.command;

import com.dfsek.terra.command.biome.BiomeCommand;
import com.dfsek.terra.command.geometry.GeometryCommand;
import com.dfsek.terra.command.image.ImageCommand;
import com.dfsek.terra.command.profile.ProfileCommand;
import com.dfsek.terra.command.structure.StructureCommand;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.command.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TerraCommand extends Command {
    private final List<Command> commands = Arrays.asList(new ReloadCommand(this),
            new BiomeCommand(this),
            new OreCommand(this),
            new ProfileCommand(this),
            new SaveDataCommand(this),
            new StructureCommand(this),
            new ImageCommand(this),
            new GeometryCommand(this),
            new FixChunkCommand(this),
            new VersionCommand(this));

    public TerraCommand(GaeaPlugin main) {
        super(main);
    }

    @Override
    public String getName() {
        return "terra";
    }

    @Override
    public List<Command> getSubCommands() {
        return commands;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        LangUtil.send("command.main-menu", sender);
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
