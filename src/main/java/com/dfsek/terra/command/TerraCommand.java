package com.dfsek.terra.command;

import com.dfsek.terra.command.image.ImageCommand;
import com.dfsek.terra.command.profile.ProfileCommand;
import com.dfsek.terra.command.structure.StructureCommand;
import com.dfsek.terra.command.type.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TerraCommand extends Command {
    private final List<com.dfsek.terra.command.type.Command> commands = Arrays.asList(new ReloadCommand(),
            new BiomeCommand(),
            new OreCommand(),
            new ProfileCommand(),
            new SaveDataCommand(),
            new StructureCommand(),
            new ImageCommand());

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
        if(args.length > 0) {
            for(com.dfsek.terra.command.type.Command c : commands) {
                if(c.getName().equals(args[0])) return c.execute(sender, command, label, Arrays.stream(args, 1, args.length).toArray(String[]::new));
            }
            sender.sendMessage("Invalid command.");
            return true;
        } else {
            sender.sendMessage("--------------------Terra--------------------");
            sender.sendMessage("reload    - Reload configuration data");
            sender.sendMessage("biome     - Get current biome");
            sender.sendMessage("ore       - Generate an ore vein at the location you are facing (For debugging)");
            sender.sendMessage("save-data - Save population data");
            sender.sendMessage("structure - Load and export structures");
            sender.sendMessage("profile   - Profiler options");
        }
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
