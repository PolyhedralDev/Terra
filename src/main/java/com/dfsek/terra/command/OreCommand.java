package com.dfsek.terra.command;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.command.type.PlayerCommand;
import com.dfsek.terra.command.type.WorldCommand;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.genconfig.OreConfig;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OreCommand extends WorldCommand {
    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        Block bl = sender.getTargetBlockExact(25);
        if(args.length > 0) {
            OreConfig ore = TerraWorld.getWorld(w).getConfig().getOre(args[0]);
            if(ore == null) {
                sender.sendMessage("Unable to find Ore");
                return true;
            }
            if(bl == null) {
                sender.sendMessage("Block out of range");
                return true;
            }
            ore.doVein(bl.getLocation(), new Random());
        } else {
            sender.sendMessage("---------------Terra/ore---------------");
            sender.sendMessage("Generates a vein of ore at the block you are looking at.");
        }
        return true;
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
    public String getName() {
        return "ore";
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
