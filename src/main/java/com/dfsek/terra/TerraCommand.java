package com.dfsek.terra;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.ConfigUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TerraCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            switch(args[0]) {
                case "reload":
                    ConfigUtil.loadConfig(Terra.getInstance());
                    sender.sendMessage("Reloaded Terra config.");
                    break;
                case "biome":
                    if(!(sender instanceof Player)) return false;
                    sender.sendMessage("You are in " + ((UserDefinedBiome) TerraBiomeGrid.fromWorld(((Player) sender).getWorld()).getBiome(((Player) sender).getLocation())).getConfig().getFriendlyName());
            }
        }
        return true;
    }
}
