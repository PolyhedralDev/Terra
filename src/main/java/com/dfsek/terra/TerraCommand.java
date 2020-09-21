package com.dfsek.terra;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.config.genconfig.OreConfig;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.WorldProfiler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TerraCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch(args[0]) {
            case "reload":
                ConfigUtil.loadConfig(Terra.getInstance());
                sender.sendMessage("Reloaded Terra config.");
                return true;
            case "biome":
                if(!(sender instanceof Player)) return false;
                sender.sendMessage("You are in " + BiomeConfig.fromBiome((UserDefinedBiome) TerraBiomeGrid.fromWorld(((Player) sender).getWorld()).getBiome(((Player) sender).getLocation())).getFriendlyName());
                return true;
            case "profile":
                if(! (sender instanceof Player)) {
                    sender.sendMessage("Command is for players only.");
                    return true;
                }
                Player p = (Player) sender;
                if(p.getWorld().getGenerator() instanceof TerraChunkGenerator) {
                    WorldProfiler profile = TerraProfiler.fromWorld(p.getWorld());
                    if(args.length > 1 && "query".equals(args[1])) {
                        sender.sendMessage(profile.getResultsFormatted());
                        return true;
                    } else if(args.length > 1 && "reset".equals(args[1])) {
                        profile.reset();
                        sender.sendMessage("Profiler has been reset.");
                        return true;
                    } else if(args.length > 1 && "start".equals(args[1])) {
                        profile.setProfiling(true);
                        sender.sendMessage("Profiler has started.");
                        return true;
                    } else if(args.length > 1 && "stop".equals(args[1])) {
                        profile.setProfiling(false);
                        sender.sendMessage("Profiler has stopped.");
                        return true;
                    }
                } else sender.sendMessage("World is not a Terra world!");
                break;
            case "ore":
                if(! (sender instanceof Player)) {
                    sender.sendMessage("Command is for players only.");
                    return true;
                }
                Block bl = ((Player) sender).getTargetBlockExact(25);
                OreConfig ore = OreConfig.fromID(args[1]);
                if(ore == null) {
                    sender.sendMessage("Unable to find Ore");
                    return true;
                }
                ore.doVein(bl.getLocation(), new Random());
                return true;

        }
        return false;
    }
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //System.out.println("Label " + label + " args: " + Arrays.toString(args));
        if(args[0].equals("tpbiome")) return BiomeConfig.getBiomeIDs();
        else if(args[0].equals("ore")) return OreConfig.getOreIDs();
        else return Collections.emptyList();
    }
}
