package com.dfsek.terra.command;

import com.dfsek.terra.api.gaea.command.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class OreCommand extends WorldCommand {
    public OreCommand(com.dfsek.terra.api.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        /*
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
            Vector source = new Vector(FastMath.floorMod(bl.getX(), 16), bl.getY(), FastMath.floorMod(bl.getZ(), 16));
            ore.doVein(source, bl.getChunk(), new FastRandom());
        } else {
            LangUtil.send("command.ore.main-menu", sender);
        }
        */
        return true;


        // TODO: implementation
    }

    @Override
    public String getName() {
        return "ore";
    }

    @Override
    public List<com.dfsek.terra.api.gaea.command.Command> getSubCommands() {
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
