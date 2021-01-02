package com.dfsek.terra.bukkit.command.command.biome;

import com.dfsek.terra.api.world.generation.GenerationPhase;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BiomeCommand extends WorldCommand {
    public BiomeCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        TerraBiomeGrid grid = getMain().getWorld(BukkitAdapter.adapt(sender.getWorld())).getGrid();
        UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome(BukkitAdapter.adapt(sender.getLocation()), GenerationPhase.POPULATE);
        LangUtil.send("command.biome.in", BukkitAdapter.adapt(sender), biome.getID());
        return true;
    }

    @Override
    public String getName() {
        return "biome";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Arrays.asList(new BiomeLocateCommand(this), new BiomeInfoCommand(this));
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
