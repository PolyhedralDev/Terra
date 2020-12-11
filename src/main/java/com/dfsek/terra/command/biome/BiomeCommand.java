package com.dfsek.terra.command.biome;

import com.dfsek.terra.api.gaea.command.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BiomeCommand extends WorldCommand {
    public BiomeCommand(com.dfsek.terra.api.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        /*
        TerraBiomeGrid grid = ((TerraBukkitPlugin) getMain()).getWorld(sender.getWorld()).getGrid();
        UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome(sender.getLocation(), GenerationPhase.POPULATE);
        LangUtil.send("command.biome.in", sender, biome.getID());

         */
        return true;
    }

    @Override
    public String getName() {
        return "biome";
    }

    @Override
    public List<com.dfsek.terra.api.gaea.command.Command> getSubCommands() {
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
