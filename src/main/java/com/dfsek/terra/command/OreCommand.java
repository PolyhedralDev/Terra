package com.dfsek.terra.command;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.config.genconfig.OreConfig;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.WorldCommand;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OreCommand extends WorldCommand {
    public OreCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        Block bl = sender.getTargetBlockExact(25);
        if(args.length > 0) {
            OreConfig ore = TerraWorld.getWorld(w).getConfig().getOre(args[0]);
            if(ore == null) {
                LangUtil.send("command.ore.invalid-ore", sender, args[0]);
                return true;
            }
            if(bl == null) {
                LangUtil.send("command.ore.out-of-range", sender);
                return true;
            }
            Vector source = new Vector(Math.floorMod(bl.getX(), 16), bl.getY(), Math.floorMod(bl.getZ(), 16));
            ore.doVein(source, bl.getChunk(), new Random());
        } else {
            LangUtil.send("command.ore.main-menu", sender);
        }
        return true;
    }

    @Override
    public String getName() {
        return "ore";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
