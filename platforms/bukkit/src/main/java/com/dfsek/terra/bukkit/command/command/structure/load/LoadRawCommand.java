package com.dfsek.terra.bukkit.command.command.structure.load;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.bukkit.BukkitChunk;
import com.dfsek.terra.bukkit.BukkitWorld;
import com.dfsek.terra.bukkit.command.DebugCommand;
import com.dfsek.terra.util.PopulationUtil;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LoadRawCommand extends LoadCommand implements DebugCommand {
    public LoadRawCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    private static void setTerraSign(Sign sign, String data) {
        sign.setLine(0, "[TERRA]");
        if(data.length() > 16) {
            sign.setLine(2, data.substring(0, 16));
            sign.setLine(3, data.substring(16));
        } else sign.setLine(2, data);
    }


    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {


        TerraWorld terraWorld = getMain().getWorld(new BukkitWorld(sender.getWorld()));
        long t = System.nanoTime();
        FastRandom chunk = PopulationUtil.getRandom(new BukkitChunk(sender.getLocation().getChunk()));

        boolean success = terraWorld.getConfig().getScriptRegistry().get(args[0]).execute(new Location(new BukkitWorld(sender.getWorld()), sender.getLocation().getX(), sender.getLocation().getY(), sender.getLocation().getZ()), chunk, Rotation.fromDegrees(90 * chunk.nextInt(4)));
        long l = System.nanoTime() - t;

        sender.sendMessage("Took " + ((double) l) / 1000000 + "ms. Success: " + success);

        return true;
    }

    @Override
    public String getName() {
        return "raw";
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
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1) {
            return getStructureNames(new BukkitWorld(((Player) commandSender).getWorld())).stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
