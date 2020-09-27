package com.dfsek.terra;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.config.WorldConfig;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import com.dfsek.terra.config.genconfig.OreConfig;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.image.WorldImageGenerator;
import com.dfsek.terra.structure.GaeaStructure;
import com.dfsek.terra.structure.InitializationException;
import com.dfsek.terra.structure.StructureSpawn;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.WorldProfiler;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TerraCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            switch(args[0]) {
                case "reload":
                    ConfigUtil.loadConfig(Terra.getInstance());
                    sender.sendMessage("Reloaded Terra config.");
                    return true;
                case "biome":
                    if(! (sender instanceof Player)) return false;
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
                case "image":
                    if("render".equals(args[1])) {
                        if(! (sender instanceof Player)) {
                            sender.sendMessage("Command is for players only.");
                            return true;
                        }
                        Player pl = (Player) sender;
                        if(args.length != 4) return false;
                        try {
                            WorldImageGenerator g = new WorldImageGenerator(pl.getWorld(), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                            g.drawWorld(pl.getLocation().getBlockX(), pl.getLocation().getBlockZ());
                            File file = new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "map" + File.separator + "map_" + System.currentTimeMillis() + ".png");
                            file.mkdirs();
                            file.createNewFile();
                            g.save(file);
                            sender.sendMessage("Saved image to " + file.getPath());
                            return true;
                        } catch(Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    } else if("gui".equals(args[1])) {
                        if(! (sender instanceof Player)) {
                            sender.sendMessage("Command is for players only.");
                            return true;
                        }
                        Player pl = (Player) sender;
                        try {
                            if("raw".equals(args[2]))
                                WorldConfig.fromWorld(pl.getWorld()).imageLoader.debug(false, pl.getWorld());
                            else if("step".equals(args[2]))
                                WorldConfig.fromWorld(pl.getWorld()).imageLoader.debug(true, pl.getWorld());
                            else return false;
                            return true;
                        } catch(NullPointerException e) {
                            return false;
                        }
                    }
                    break;
                case "save-data":
                    TerraChunkGenerator.saveAll();
                    sender.sendMessage("Saved population data.");
                    return true;
                case "structure":
                    if(! (sender instanceof Player)) {
                        sender.sendMessage("Command is for players only.");
                        return true;
                    }

                    Player pl = (Player) sender;
                    if("export".equals(args[1])) {
                        WorldEditPlugin we = WorldEditUtil.getWorldEdit();
                        if(we == null) {
                            sender.sendMessage("WorldEdit is not installed! Please install WorldEdit before attempting to export structures.");
                            return true;
                        }
                        Region selection;
                        try {
                            selection = we.getSession(pl).getSelection(BukkitAdapter.adapt(pl.getWorld()));
                        } catch(IncompleteRegionException e) {
                            sender.sendMessage("Invalid/incomplete selection!");
                            return true;
                        }
                        BukkitAdapter.adapt(pl);
                        if(selection == null) {
                            sender.sendMessage("Please make a selection before attempting to export!");
                            return true;
                        }
                        BlockVector3 min = selection.getMinimumPoint();
                        BlockVector3 max = selection.getMaximumPoint();
                        Location l1 = new Location(pl.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
                        Location l2 = new Location(pl.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
                        GaeaStructure structure = null;
                        try {
                            structure = new GaeaStructure(l1, l2, args[2]);
                        } catch(InitializationException e) {
                            sender.sendMessage(e.getMessage());
                            return true;
                        }
                        try {
                            File file = new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures", args[2] + ".tstructure");
                            file.getParentFile().mkdirs();
                            file.createNewFile();
                            structure.save(file);
                            sender.sendMessage("Saved structure with ID " + structure.getId() + ", UUID: " + structure.getUuid().toString() + " to " + file.getPath());
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else if("load".equals(args[1])) {
                        try {
                            GaeaStructure struc = GaeaStructure.load(new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures", args[2] + ".tstructure"));
                            if("true".equals(args[3])) struc.paste(pl.getLocation());
                            else struc.paste(pl.getLocation(), pl.getLocation().getChunk());
                        } catch(IOException e) {
                            e.printStackTrace();
                            sender.sendMessage("Structure not found.");
                        }
                        return true;
                    } else if("getspawn".equals(args[1])) {
                        Vector v = new StructureSpawn(250, 250).getNearestSpawn(pl.getLocation().getBlockX(), pl.getLocation().getBlockZ(), pl.getWorld().getSeed());
                        sender.sendMessage(v.getBlockX() + ":" + v.getBlockZ());
                    }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
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
