package com.dfsek.terra.bukkit.command.command.biome;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.population.items.TerraStructure;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BiomeInfoCommand extends WorldCommand {
    public BiomeInfoCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        String id = args[0];
        ConfigPack cfg = getMain().getWorld(BukkitAdapter.adapt(world)).getConfig();
        UserDefinedBiome b;
        try {
            b = cfg.getBiome(id);
        } catch(IllegalArgumentException | NullPointerException e) {
            LangUtil.send("command.biome.invalid", new BukkitCommandSender(sender), id);
            return true;
        }
        sender.sendMessage("TerraBiome info for \"" + b.getID() + "\".");
        sender.sendMessage("Vanilla biome: " + b.getVanillaBiomes());
        sender.sendMessage("Eroded by: " + b.getErode().getConfig().getID());


        BiomeTemplate bio = b.getConfig();

        if(bio.getExtend() != null) sender.sendMessage("Extends: " + bio.getExtend());

        List<TerraStructure> structureConfigs = bio.getStructures();

        if(structureConfigs.size() == 0) sender.sendMessage("No Structures");
        else {
            sender.sendMessage("-------Structures-------");
            for(TerraStructure c : structureConfigs) {
                sender.sendMessage(" - " + c.getTemplate().getID());
            }
        }

        Map<UserDefinedCarver, Integer> carverConfigs = bio.getCarvers();

        if(structureConfigs.size() == 0) sender.sendMessage("No Carvers");
        else {
            sender.sendMessage("---------Carvers--------");
            for(Map.Entry<UserDefinedCarver, Integer> entry : carverConfigs.entrySet()) {
                sender.sendMessage(" - " + entry.getKey().getConfig().getID() + ": " + entry.getValue() + "%");
            }
        }

        return true;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(!(sender instanceof Player) || !TerraWorld.isTerraWorld(BukkitAdapter.adapt(((Player) sender).getWorld())))
            return Collections.emptyList();
        List<String> ids = getMain().getWorld(BukkitAdapter.adapt(((Player) sender).getWorld())).getConfig().getBiomeIDs();
        if(args.length == 1)
            return ids.stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
