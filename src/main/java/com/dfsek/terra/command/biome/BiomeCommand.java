package com.dfsek.terra.command.biome;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.WorldCommand;
import org.polydev.gaea.generation.GenerationPhase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BiomeCommand extends WorldCommand {
    public BiomeCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        TerraBiomeGrid grid = TerraWorld.getWorld(sender.getWorld()).getGrid();
        UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome(sender.getLocation(), GenerationPhase.POPULATE);
        LangUtil.send("command.biome.in", sender, TerraWorld.getWorld(w).getConfig().getBiome(biome).getID());
        return true;
    }

    @Override
    public String getName() {
        return "biome";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Arrays.asList(new BiomeLocateCommand(this, true), new BiomeLocateCommand(this, false), new BiomeInfoCommand(this));
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
