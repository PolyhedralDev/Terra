package com.dfsek.terra.command;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GenerationPhase;

import java.util.Collections;
import java.util.List;

public class BiomeCommand extends PlayerCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TerraBiomeGrid grid = TerraBiomeGrid.fromWorld(sender.getWorld());
        if(grid == null) {
            sender.sendMessage("Not a Terra world!");
            return true;
        }
        UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome(sender.getLocation(), GenerationPhase.POPULATE);
        sender.sendMessage("You are in " + BiomeConfig.fromBiome(biome).getID());
        return true;
    }

    @Override
    public String getName() {
        return "biome";
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return Collections.emptyList();
    }
}
