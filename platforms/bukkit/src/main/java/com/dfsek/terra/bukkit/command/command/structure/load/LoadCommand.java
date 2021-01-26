package com.dfsek.terra.bukkit.command.command.structure.load;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.bukkit.command.DebugCommand;
import com.dfsek.terra.bukkit.command.PlayerCommand;
import com.dfsek.terra.world.TerraWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoadCommand extends PlayerCommand implements DebugCommand {
    public LoadCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    public List<String> getStructureNames(World world) {
        List<String> names = new ArrayList<>();
        TerraWorld terraWorld = getMain().getWorld(world);

        terraWorld.getConfig().getScriptRegistry().forEach(script -> names.add(script.getId()));

        return names;
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }

    @Override
    public String getName() {
        return "load";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Arrays.asList(new LoadFullCommand(this, true), new LoadFullCommand(this, false));
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 0;
    }
}
