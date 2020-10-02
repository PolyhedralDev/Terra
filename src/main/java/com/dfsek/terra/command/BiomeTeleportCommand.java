package com.dfsek.terra.command;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.command.type.WorldCommand;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BiomeTeleportCommand extends WorldCommand {
    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        return false;
    }

    @Override
    public String getName() {
        return "tpbiome";
    }

    @Override
    public List<com.dfsek.terra.command.type.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(!(sender instanceof  Player) || !(((Player) sender).getWorld().getGenerator() instanceof TerraChunkGenerator)) return Collections.emptyList();
        return TerraWorld.getWorld(((Player) sender).getWorld()).getConfig().getBiomeIDs();
    }
}
